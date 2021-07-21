package com.wudgaby.platform.flowable.helper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wudgaby.platform.flowable.helper.enums.ProcessType;
import com.wudgaby.platform.flowable.helper.form.DeployForm;
import com.wudgaby.platform.flowable.helper.form.ProcessDefinitionQueryForm;
import com.wudgaby.platform.flowable.helper.vo.ProcessDefinitionVo;
import org.flowable.engine.repository.Deployment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName : IFlowableProcessDefinitionService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/18 16:52
 * @Desc :
 */
public interface FlowableProcessDefinitionService {
    /**
     * 通过条件查询流程定义
     *
     * @param params
     * @return
     */
    IPage<ProcessDefinitionVo> getListByPage(ProcessDefinitionQueryForm queryForm);

    /**
     * 通过流程定义id获取流程定义的信息
     *
     * @param processDefinitionId 流程定义id
     * @return
     */
    ProcessDefinitionVo getById(String processDefinitionId);

    /**
     * 挂起流程定义
     *
     * @param processDefinitionId 流程定义id
     * @param suspensionState     状态1挂起 2激活
     */
    void suspendOrActivateProcessDefinitionById(String processDefinitionId, int suspensionState);

    /**
     * 部署流程
     * @return
     */
    Deployment deploy(ProcessType processType, String processName, MultipartFile file) throws IOException;

    /**
     * 部署流程
     * @return
     */
    Deployment deploy(DeployForm deployForm);
}
