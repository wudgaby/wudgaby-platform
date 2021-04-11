package com.wudgaby.flowable.module.web.controller;

import com.wudgaby.flowable.module.web.flowable.IFlowableModelService;
import com.wudgaby.flowable.module.web.flowable.impl.FlowProcessDiagramGenerator;
import com.wudgaby.flowable.module.web.vo.ModelVo;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.idm.api.User;
import org.flowable.ui.modeler.domain.AbstractModel;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/rest/model")
public class ApiFlowableModelResource{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiFlowableModelResource.class);
    @Autowired
    private ModelService modelService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private IFlowableModelService flowableModelService;
    @Autowired
    private FlowProcessDiagramGenerator flowProcessDiagramGenerator;

    @GetMapping(value = "/bpmnModels")
    public List<AbstractModel> pageModel() {
        List<AbstractModel> datas = modelService.getModelsByModelType(AbstractModel.MODEL_TYPE_BPMN);
        datas.forEach(abstractModel -> {
            User user = identityService.createUserQuery().userId(abstractModel.getCreatedBy()).singleResult();
            abstractModel.setCreatedBy(user.getFirstName());
        });
        return datas;
    }

    @PostMapping(value = "/addModel")
    public void addModel(@RequestBody ModelVo params) {
        flowableModelService.addModel(params);
    }

    @PostMapping(value = "/import-process-model")
    public void importProcessModel(@RequestParam("file") MultipartFile file) {
        flowableModelService.importProcessModel(file);
    }

    @PostMapping(value = "/deploy")
    public void deploy(String modelId) {
        Model model = modelService.getModel(modelId.trim());
        //到时候需要添加分类
        String categoryCode = "1000";
        BpmnModel bpmnModel = modelService.getBpmnModel(model);
        //添加隔离信息
        String tenantId = "flow";
        //必须指定文件后缀名否则部署不成功
        Deployment deploy = repositoryService.createDeployment()
                .name(model.getName())
                .key(model.getKey())
                .category(categoryCode)
                .tenantId(tenantId)
                .addBpmnModel(model.getKey() + ".bpmn", bpmnModel)
                .deploy();
    }
    /**
     * 显示xml
     *
     * @param modelId
     * @return
     */
    @GetMapping(value = "/loadXmlByModelId/{modelId}")
    public void loadXmlByModelId(@PathVariable String modelId, HttpServletResponse response) throws IOException {
        Model model = modelService.getModel(modelId);
        byte[] b = modelService.getBpmnXML(model);
        response.setHeader("Content-type", "text/xml;charset=UTF-8");
        response.getOutputStream().write(b);
    }


    @GetMapping(value = "/loadPngByModelId/{modelId}")
    public void loadPngByModelId(@PathVariable String modelId, HttpServletResponse response) {
        Model model = modelService.getModel(modelId);
        BpmnModel bpmnModel = modelService.getBpmnModel(model, new HashMap<>(), new HashMap<>());
        InputStream is = flowProcessDiagramGenerator.generateDiagram(bpmnModel);
        try {
            response.setHeader("Content-Type", "image/png");
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        } catch (Exception e) {
            LOGGER.error("ApiFlowableModelResource-loadPngByModelId:" + e);
            e.printStackTrace();
        }
    }
}
