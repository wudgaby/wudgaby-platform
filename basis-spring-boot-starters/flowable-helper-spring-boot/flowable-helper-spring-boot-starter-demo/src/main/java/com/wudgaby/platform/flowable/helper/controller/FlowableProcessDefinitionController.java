package com.wudgaby.platform.flowable.helper.controller;

import cn.hutool.core.io.file.FileWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.entity.FlowConfig;
import com.wudgaby.platform.flowable.helper.entity.FlowNode;
import com.wudgaby.platform.flowable.helper.enums.ExportBpmnType;
import com.wudgaby.platform.flowable.helper.enums.ProcessType;
import com.wudgaby.platform.flowable.helper.form.DeployForm;
import com.wudgaby.platform.flowable.helper.form.ProcessDefinitionQueryForm;
import com.wudgaby.platform.flowable.helper.service.FlowConfigService;
import com.wudgaby.platform.flowable.helper.service.FlowNodeService;
import com.wudgaby.platform.flowable.helper.service.FlowableProcessDefinitionService;
import com.wudgaby.platform.flowable.helper.service.impl.FlowableCustomBpmnModelService;
import com.wudgaby.platform.flowable.helper.service.impl.FlowableNodeService;
import com.wudgaby.platform.flowable.helper.vo.ProcessDefinitionVo;
import com.wudgaby.platform.flowable.helper.vo.flow.FlowConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @ClassName : flowableController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/18 14:26
 * @Desc :   管理flowable流程部署
 */
@Api(tags = "流程定义管理")
@RestController
@RequestMapping("/workflow/definitions")
@AllArgsConstructor
@Slf4j
public class FlowableProcessDefinitionController {
    private final RepositoryService repositoryService;
    private final FlowableProcessDefinitionService flowableProcessDefinitionService;
    private final FlowableNodeService flowableNodeService;
    private final Environment environment;
    private final FlowableCustomBpmnModelService flowableBpmnModelService;

    private final FlowConfigService flowConfigService;
    private final FlowNodeService flowNodeService;
    private final FlowableCustomBpmnModelService flowableCustomBpmnModelService;

    @GetMapping
    @ApiOperation(value = "获取部署列表")
    public ApiPageResult<ProcessDefinitionVo> delDeployment(ProcessDefinitionQueryForm queryForm) throws IOException {
        IPage<ProcessDefinitionVo> iPage = flowableProcessDefinitionService.getListByPage(queryForm);
        return ApiPageResult.success(iPage);
    }

    @PostMapping(value = "/deploy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "根据上传的文件部署流程")
    public ApiResult deployFromBpmnFile(@RequestParam ProcessType processType,
                                        @RequestParam String processName,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        Deployment deployment = flowableProcessDefinitionService.deploy(processType, processName, file);
        return ApiResult.success().message("部署流程成功.").data(Optional.ofNullable(deployment.getId()).orElse(StringUtils.EMPTY));
    }

    @PostMapping(value = "/deployFromFile")
    @ApiOperation(value = "部署流程")
    public ApiResult deployFromFile(@RequestBody DeployForm deployForm){
        Deployment deployment = flowableProcessDefinitionService.deploy(deployForm);
        return ApiResult.success().message("部署流程成功.").data(Optional.ofNullable(deployment.getId()).orElse(StringUtils.EMPTY));
    }

    @DeleteMapping
    @ApiOperation(value = "删除部署")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署id"),
            @ApiImplicitParam(name = "forceDel", value = "false:不带级联的删除.只能删除没有启动的流程，进行中的流程,删除会抛异常. 已经完成的流程可以删除" +
                                                            "true 级联删除：不管流程是否启动，都可以删除")
    })
    public ApiResult delDeployment(@RequestParam String deploymentId, @RequestParam Boolean forceDel){
        try {
            //生产环境强制不允许删除运行中的部署流程.
            if(ArrayUtils.contains(environment.getActiveProfiles(), "prod")){
                forceDel = false;
            }
            repositoryService.deleteDeployment(deploymentId, forceDel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure().message("该流程已启动.不能删除");
        }
        return ApiResult.success().message("删除部署成功.");
    }

    /**
     * 通过id和类型获取图片
     *
     * @param processDefId     流程定义id
     * @param type     类型
     * @param response response
     */
    @GetMapping(value = "/processFile", produces = MediaType.IMAGE_JPEG_VALUE)
    @ApiOperation(value = "获取流程定义文件", notes = "通过id和类型获取流程定义的xml或图片.procDefId = mylove:1:f7c54432a3e94ce2874e0a001b7be421")
    public void processFile(@RequestParam(required = false) String procDefId,
                            @RequestParam(required = false) String depId,
                            @RequestParam ExportBpmnType type, HttpServletResponse response) {
        try {
            byte[] b = null;
            ProcessDefinition pd = null;
            if(StringUtils.isNotBlank(procDefId)){
                pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
            } else {
                pd = repositoryService.createProcessDefinitionQuery().deploymentId(depId).singleResult();
            }
            if (pd != null) {
                if (type == ExportBpmnType.XML) {
                    response.setHeader("Content-type", MediaType.APPLICATION_XML_VALUE);
                    response.setHeader("Content-Disposition", "attachment;filename=flowdemo.bpmn20.xml");
                    InputStream inputStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getResourceName());
                    b = IoUtil.readInputStream(inputStream, "image inputStream name");
                } else if (type == ExportBpmnType.IMAGE){
                    //加了这个swagger bootstrap ui 无法显示图片
                    response.setHeader("Content-Type", MediaType.IMAGE_JPEG_VALUE);
                    InputStream inputStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
                    b = IoUtil.readInputStream(inputStream, "image inputStream name");
                }
                response.getOutputStream().write(b);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @GetMapping(value = "/bpmn")
    @ApiOperation(value = "bpmn描述")
    public void bpmn(@RequestParam String processDefId, @RequestParam String proInsId, HttpServletResponse response) throws IOException {
        BpmnModel bpmnModel = null;
        try{
            bpmnModel = repositoryService.getBpmnModel(processDefId);
        }catch (FlowableObjectNotFoundException e){
            throw new BusinessException("没有找到已部署的流程定义");
        }
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] exportBytes = xmlConverter.convertToXML(bpmnModel);
        response.getOutputStream().write(exportBytes);

        Process process = bpmnModel.getMainProcess();
        Collection<FlowElement> flowElements = process.getFlowElements();
        List<UserTask> userTasks = new ArrayList<>();
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask)flowElement;
                userTasks.add(userTask);
            }
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow)flowElement;
            }
            if (flowElement instanceof ExclusiveGateway) {
                ExclusiveGateway exclusiveGateway = (ExclusiveGateway)flowElement;
            }
            log.info(flowElement.getClass() + " - " +flowElement.getId() + ":" + flowElement.getName());
        }

        //获得流程节点信息
        Map<String, GraphicInfo> locationMap = bpmnModel.getLocationMap();
        //获得流程节点之间连线信息
        Map<String, List<GraphicInfo>> flowLocationMap = bpmnModel.getFlowLocationMap();
    }

    @GetMapping(value = "/dynamicsNode")
    @ApiOperation(value = "动态计算节点信息")
    public ApiResult dynamicsNode(@RequestParam String proInsId){
        return ApiResult.success().data(flowableNodeService.dynamicsNode(proInsId));
    }

    @PostMapping(value = "/createCustomBpmnModel")
    @ApiOperation(value = "动态生成流程定义")
    public ApiResult createCustomBpmnModel(@RequestParam ProcessType processType, @RequestParam String processName, @Validated @RequestBody FlowConfigVo flowConfigVo){
        BpmnModel bpmnModel = flowableBpmnModelService.createCustomBpmnModel(flowConfigVo);
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        FileWriter.create(new File("D:\\customer.bpmn20.xml"), Charsets.UTF_8).write(bpmnBytes,0, bpmnBytes.length);

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .category(processType.name())
                .name(processName)
                .tenantId(FlowConstant.TENANT)
                //.addBytes(processName + ".bpmn20.xml", bpmnBytes)
                .addBpmnModel(processType.name()+ ".bpmn20.xml", bpmnModel)
                ;

        Deployment deployment = deploymentBuilder.deploy();

        if(deployment != null){
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            if(processDefinition == null){
                throw new BusinessException("部署失败.");
            }
            repositoryService.setProcessDefinitionCategory(processDefinition.getId(), processType.name());
            return ApiResult.success().data(deployment.getId());
        }

        return ApiResult.failure();
    }

    @ApiOperation("部署自定义流程")
    @PostMapping
    public ApiResult add(@RequestBody FlowConfigVo flowConfigVo){
        FlowConfig flowConfig = new FlowConfig();
        flowConfig.setAutoApprove(flowConfigVo.getAutoApprove());
        flowConfig.setAutoRepeat(flowConfigVo.getAutoRepeat());
        flowConfig.setOptimized(flowConfigVo.getOptimized());
        flowConfig.setCommentHint(flowConfigVo.getCommentHint());
        flowConfig.setDesc(flowConfigVo.getDesc());
        flowConfig.setName(flowConfigVo.getName());
        flowConfig.setGroupName(flowConfigVo.getGroup());
        flowConfig.setRejectToInitiator(flowConfigVo.getRejectToInitiator());
        flowConfigService.save(flowConfig);

        AtomicInteger index = new AtomicInteger(1);
        List<FlowNode> flowNodeList = flowConfigVo.getNodeConfigList().stream().map(node -> {
            FlowNode flowNode = new FlowNode();
            flowNode.setAllowOptional(node.getAllowOptional());
            flowNode.setApportionType(node.getApportionType().name());
            flowNode.setAssignIds(Joiner.on(",").skipNulls().join(node.getIdList()));
            flowNode.setAssignName(Joiner.on(",").skipNulls().join(node.getNameList()));
            flowNode.setExpr(node.getExpr());
            flowNode.setLevel(node.getLevel());
            flowNode.setNodeName(node.getNodeName());
            flowNode.setNodeType(node.getNodeType().name());
            //flowNode.setNodeKey(node.getNodeType().name() + UUID.fastUUID().toString(true));
            flowNode.setNodeKey(node.getNodeType().name() + index.get());
            flowNode.setFlowId(flowConfig.getId());
            flowNode.setSignType(node.getSignType().name());

            node.setNodeKey(flowNode.getNodeKey());
            index.incrementAndGet();
            return flowNode;
        }).collect(Collectors.toList());
        flowNodeService.saveBatch(flowNodeList);

        String depId = deploy(flowConfigVo);

        FlowConfig updateFlowConfig = new FlowConfig();
        updateFlowConfig.setId(flowConfig.getId());
        updateFlowConfig.setDefId(depId);
        flowConfigService.updateById(updateFlowConfig);

        return ApiResult.success().data(depId);
    }

    private boolean validateBpmnModel(BpmnModel bpmnModel){
        ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
        ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
        //验证失败信息的封装ValidationError
        List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
        log.error(validate.toString());

        return validate.size() <= 0;
    }

    private String deploy(FlowConfigVo flowConfigVo){
        BpmnModel bpmnModel = flowableCustomBpmnModelService.createCustomBpmnModel(flowConfigVo);
        if(!validateBpmnModel(bpmnModel)){
            throw new BusinessException("bpmnModel 校验失败.");
        }

        /*byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        FileWriter.create(new File("D:\\customer.bpmn20.xml"), Charsets.UTF_8).write(bpmnBytes,0, bpmnBytes.length);*/

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .category(flowConfigVo.getGroup())
                .name(flowConfigVo.getName())
                .key(flowConfigVo.getDefKey())
                .tenantId(FlowConstant.TENANT)
                //.addBytes(processName + ".bpmn20.xml", bpmnBytes)
                .addBpmnModel(flowConfigVo.getDefKey()+ ".bpmn20.xml", bpmnModel)
                ;

        Deployment deployment = deploymentBuilder.deploy();

        if(deployment != null){
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            if(processDefinition == null){
                throw new BusinessException("部署失败.");
            }
            repositoryService.setProcessDefinitionCategory(processDefinition.getId(), flowConfigVo.getGroup());
            return deployment.getId();
        }
        return "";
    }
}
