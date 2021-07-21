package com.wudgaby.platform.flowable.helper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wudgaby.platform.flowable.helper.vo.*;
import org.flowable.engine.runtime.ProcessInstance;

/**
 * @ClassName : IFlowableProcessInstanceService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/19 9:14
 * @Desc :
 */
 public interface FlowableProcessInstanceService {
    /**
     * 启动流程
     *
     * @param startProcessInstanceVo 参数
     * @return
     */
    ProcessInstance startProcessInstanceByKey(StartProcessInstanceVo startProcessInstanceVo);

    /**
     * 查询流程实例列表
     *
     * @param params 参数
     * @param query  分页参数
     * @return
     */
    IPage<ProcessInstanceVo> getProcessInstances(ProcessInstanceQueryVo queryForm);

    /**
     * 获取单个实例
     * @param processInstanceId
     * @return
     */
    ProcessInstanceVo getProcessInstance(String processInstanceId);

    /**
     * 获取流程图图片
     *
     * @param processInstanceId 流程实例id
     * @return
     */
     byte[] createImage(String processInstanceId);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例id
     * @return
     */
     void deleteProcessInstanceById(String processInstanceId);


    /**
     * 终止流程
     * @param endVo 参数
     * @return
     */
     void stopProcessInstanceById(EndProcessVo endVo) ;

    /**
     * 撤回流程
     * @param revokeVo 参数
     * @return
     */
    void revokeProcess(RevokeProcessVo revokeVo);
}
