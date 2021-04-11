package com.wudgaby.platform.flowable.helper.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.result.ApiPageResult;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.consts.FlowableConst;
import com.wudgaby.platform.flowable.helper.entity.FlowConfig;
import com.wudgaby.platform.flowable.helper.entity.FlowNode;
import com.wudgaby.platform.flowable.helper.entity.UserTbl;
import com.wudgaby.platform.flowable.helper.enums.CommentTypeEnum;
import com.wudgaby.platform.flowable.helper.form.AddCcForm;
import com.wudgaby.platform.flowable.helper.service.EmployeeService;
import com.wudgaby.platform.flowable.helper.service.FlowConfigService;
import com.wudgaby.platform.flowable.helper.service.FlowNodeService;
import com.wudgaby.platform.flowable.helper.service.FlowableProcessInstanceService;
import com.wudgaby.platform.flowable.helper.service.FlowableTaskService;
import com.wudgaby.platform.flowable.helper.vo.EndProcessVo;
import com.wudgaby.platform.flowable.helper.vo.ProcessInstanceQueryVo;
import com.wudgaby.platform.flowable.helper.vo.ProcessInstanceVo;
import com.wudgaby.platform.flowable.helper.vo.StartProcessInstanceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.IdmIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : bruce.liu
 * @title: : ApiTask
 * @projectName : flowable
 * @description: 流程实例API
 * @date : 2019/11/1321:21
 */
@Api(tags = "流程实例管理")
@RestController
@RequestMapping("/flowable/processInstances")
public class FlowableProcessInstanceController{
    @Autowired
    private FlowableProcessInstanceService flowableProcessInstanceService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FlowNodeService flowNodeService;
    @Autowired
    private FlowConfigService flowConfigService;
    @Autowired
    private FlowableTaskService flowableTaskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdmIdentityService idmIdentityService;

    @GetMapping("/ccToMe")
    @ApiOperation("抄送给我的审批流程")
    public ApiResult cc2Me(@RequestParam(required = false) String userId){
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        query.involvedUser(userId, FlowableConst.CC);
        return ApiResult.success(query.list());
    }

    @ApiOperation("获取流程列表")
    @GetMapping
    public ApiPageResult<ProcessInstanceVo> list(ProcessInstanceQueryVo queryVo) {
        IPage<ProcessInstanceVo> iPage = flowableProcessInstanceService.getProcessInstances(queryVo);
        return ApiPageResult.success(iPage);
    }

    @ApiOperation("获取流程实例")
    @GetMapping(value = "/{processInstanceId}")
    public ApiResult<ProcessInstanceVo> getProcessInstance(@PathVariable String processInstanceId) {
        return ApiResult.<ProcessInstanceVo>success().data(flowableProcessInstanceService.getProcessInstance(processInstanceId));
    }

    @ApiOperation("删除流程实例")
    @DeleteMapping(value = "/{processInstanceId}")
    public ApiResult deleteProcessInstanceById(@PathVariable String processInstanceId) {
        flowableProcessInstanceService.deleteProcessInstanceById(processInstanceId);
        return ApiResult.success().message("删除成功");
    }

    @ApiOperation(value = "终止流程")
    @PostMapping(value = "/stopProcess")
    public ApiResult stopProcess(@RequestBody EndProcessVo params) {
        params.setMessage("后台执行终止");
        params.setUserCode("userId");
        flowableProcessInstanceService.stopProcessInstanceById(params);
        return ApiResult.success().message("已终止");
    }

    @ApiOperation(value = "流程追踪")
    @GetMapping(value = "/processImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public void processInstanceImage(HttpServletResponse response, @RequestParam String processInstanceId) throws IOException {
        response.setHeader("Content-Type", MediaType.IMAGE_JPEG_VALUE);
        byte[] b = flowableProcessInstanceService.createImage(processInstanceId);
        response.getOutputStream().write(b);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }


    @PostMapping("/start")
    @ApiOperation("发起流程")
    public ApiResult start(@RequestParam String procDefKey, @RequestBody List<AddCcForm> addCcFormList){
        UserTbl currentUser = new UserTbl();
        currentUser.setUserId("1");

        StartProcessInstanceVo startProcessInstanceVo = new StartProcessInstanceVo();
        startProcessInstanceVo.setBusinessKey("123");
        startProcessInstanceVo.setCreator(String.valueOf(currentUser.getUserId()));
        startProcessInstanceVo.setCurrentUserCode(String.valueOf(currentUser.getUserId()));
        startProcessInstanceVo.setFormName("");
        startProcessInstanceVo.setSystemSn(FlowConstant.TENANT);
        startProcessInstanceVo.setProcessDefinitionKey(procDefKey);
        startProcessInstanceVo.setCommentTypeEnum(CommentTypeEnum.TJ);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procDefKey).latestVersion().singleResult();
        if(processDefinition == null) {
            throw new BusinessException("流程未定义");
        }

        FlowConfig flowConfig = flowConfigService.getOne(Wrappers.<FlowConfig>lambdaQuery().eq(FlowConfig::getDefId, processDefinition.getDeploymentId()));
        List<FlowNode> flowNodeList = flowNodeService.list(Wrappers.<FlowNode>lambdaQuery().eq(FlowNode::getFlowId, flowConfig.getId()));

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("flowConfig", flowConfig);
        startProcessInstanceVo.setVariables(variables);

        Map<String, List<String>> map = Optional.ofNullable(addCcFormList).orElse(Lists.newArrayList())
                                .stream()
                                .collect(Collectors.toMap(item -> item.getNodeKey(), item -> item.getCcList()));

        //发起流程时, 确认多实例, 可动态增加抄送人
        for (FlowNode flowNode : flowNodeList){
            /*boolean isMultiUsers = flowNode.getApportionType() .equals(NodeConfigVo.ApprovalType.USER.name())
                    && CollectionUtils.size(Splitter.on(",").trimResults().splitToList(flowNode.getAssignIds())) > 1;
            boolean isRole = flowNode.getApportionType().equals(NodeConfigVo.ApprovalType.ROLE.name());

            List<String> assigneeList = Lists.newArrayList();
            if(isMultiUsers){
                assigneeList = Splitter.on(",").splitToList(flowNode.getAssignIds());
            }
            //加载角色用户
            else if(isRole){
                List<User> userList = idmIdentityService.createUserQuery().memberOfGroups(Splitter.on(",").splitToList(flowNode.getAssignIds())).list();
                if(CollectionUtils.isNotEmpty(userList)){
                    assigneeList = userList.stream().map(User::getId).collect(Collectors.toList());
                }
            } else {
                assigneeList = Lists.newArrayList(Splitter.on(",").trimResults().split(flowNode.getAssignIds()));
                if(map.containsKey(flowNode.getNodeKey())){
                    assigneeList.addAll(map.get(flowNode.getNodeKey()));
                }
            }*/

            //List<String> assigneeList = Lists.newArrayList(Splitter.on(",").trimResults().split(flowNode.getAssignIds()));
            List<String> assigneeList = Lists.newArrayList();
            if(map.containsKey(flowNode.getNodeKey())){
                assigneeList.addAll(map.get(flowNode.getNodeKey()));
            }
            variables.put(flowNode.getNodeKey() + FlowConstant.VAR_ASSIGNEE_LIST_KEY , assigneeList);
        }

        ProcessInstance processInstance = flowableProcessInstanceService.startProcessInstanceByKey(startProcessInstanceVo);
        /*if(CollectionUtils.isNotEmpty(assigneeList)){
            //加签抄送人
            runtimeService.addMultiInstanceExecution("a", processInstance.getId(), Collections.singletonMap("assignee", assigneeList));
        }*/

        /*if(flowConfig.getRejectToInitiator()){
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
            for (Task task : tasks) {
                // 约定：发起者节点为 initiateUserTask ,则自动完成任务
                if (FlowConstant.FLOW_INITIATOR_DEF_KEY.equals(task.getTaskDefinitionKey())) {
                    if (ObjectUtils.isEmpty(task.getAssignee())) {
                        taskService.setAssignee(task.getId(), currentUser.getUserId());
                    }
                    TaskCompleteForm taskCompleteForm = new TaskCompleteForm();
                    taskCompleteForm.setTaskId(task.getId());
                    taskCompleteForm.setAuditType(AuditType.pass);
                    flowableTaskService.complete(taskCompleteForm, currentUser.getUserId());
                }
            }
        }*/
        return ApiResult.success().data(processInstance.getId());
    }
}
