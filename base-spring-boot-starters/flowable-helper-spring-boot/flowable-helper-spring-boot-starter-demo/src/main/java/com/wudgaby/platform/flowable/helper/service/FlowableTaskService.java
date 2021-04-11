package com.wudgaby.platform.flowable.helper.service;

import com.github.pagehelper.Page;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.flowable.helper.form.RejectForm;
import com.wudgaby.platform.flowable.helper.form.TaskCompleteForm;
import com.wudgaby.platform.flowable.helper.vo.*;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;

import java.util.Collection;
import java.util.List;

/**
 * @author : bruce.liu
 * @projectName : flowable
 * @description: 运行时的任务service
 * @date : 2019/11/1315:05
 */
public interface FlowableTaskService {

    /**
     * 驳回任意节点 暂时没有考虑子流程
     *
     * @param backTaskVo 参数
     * @return
     */
    void backToStepTask(BackTaskVo backTaskVo);

    /**
     * 获取可驳回节点列表
     * @param taskId 任务id
     * @param processInstanceId 流程实例id
     * @return
     */
    List<FlowNodeVo> getBackNodesByProcessInstanceId(String processInstanceId, String taskId) ;
    /**
     * 任务前加签 （如果多次加签只能显示第一次前加签的处理人来处理任务）
     *
     * @param addSignTaskVo 参数
     * @return
     */
    void beforeAddSignTask(AddSignTaskVo addSignTaskVo);

    /**
     * 任务后加签
     *
     * @param addSignTaskVo 参数
     * @return
     */
    void afterAddSignTask(AddSignTaskVo addSignTaskVo);

    /**
     * 任务加签
     *
     * @param addSignTaskVo 参数
     * @param flag  true向后加签  false向前加签
     * @return
     */
    void addSignTask(AddSignTaskVo addSignTaskVo, Boolean flag);

    /**
     * 反签收任务
     *
     * @param claimTaskVo 参数
     * @return
     */
    void unClaimTask(ClaimTaskVo claimTaskVo);

    /**
     * 签收任务
     *
     * @param claimTaskVo 参数
     * @return
     */
    void claimTask(ClaimTaskVo claimTaskVo);

    /**
     * 委派任务
     *
     * @param delegateTaskVo 参数
     * @return
     */
    void delegateTask(DelegateTaskVo delegateTaskVo);

    /**
     * 转办
     *
     * @param turnTaskVo 转办任务VO
     * @return 返回信息
     */
    void turnTask(TurnTaskVo turnTaskVo);

    /**
     * 执行任务
     *
     * @param params 参数
     */
    void complete(CompleteTaskVo params);

    /**
     * 通过任务id获取任务对象
     *
     * @param taskId 任务id
     * @return
     */
    Task findTaskById(String taskId);

    /**
     * 查询待办任务列表
     *
     * @param params 参数
     * @return
     */
    Page<TaskVo> getApplyingTasks(TaskQueryVo params, PageForm pageForm);

    /**
     * 查询已办任务列表
     *
     * @param params 参数
     * @return
     */
    Page<TaskVo> getApplyedTasks(TaskQueryVo params, PageForm pageForm);

    /**
     * 通过流程实例id获取流程实例的待办任务审批人列表
     *
     * @param processInstanceId 流程实例id
     * @return
     */
    List<User> getApprovers(String processInstanceId);

    /**
     * 获取流程实例所有审批人列表
     * @param processInstanceId
     * @return
     */
    List<User> getHisApprovers(String processInstanceId);

    /**
     * 获取任务审批人列表
     * @param taskId
     * @return
     */
    List<User> getTaskApprovers(String taskId);

    /**
     * 通过任务id判断当前节点是不是并行网关的节点
     * @param taskId 任务id
     * @return
     */
    boolean checkParallelgatewayNode(String taskId) ;

    /**
     * 获取当前最新定义的流程和进行中的任务的受理人
     * @return
     */
    Collection<IdentityVo> getApproverAndRoleList();

    /**
     * 办理任务(审批)
     */
    void complete(TaskCompleteForm taskCompleteForm, String userId);

    /**
     * 撤销任务
     */
    void repeal(RejectForm rejectForm, String userId);

    /**
     * 终止流程
     */
    void stopProcessInstance(EndProcessVo endProcessVo);

    /**
     * 查询可退回节点
     * @param taskId
     * @return
     */
    List<FlowNodeVo> getBackNodes(String taskId);

    /**
     * 回退节点
     * @param backTaskVo
     */
    void backTask(BackTaskVo backTaskVo);

    /**
     * 任务已阅
     * @param readTaskVo
     */
    void readTask(ReadTaskVo readTaskVo);

    void addSign(AddSubSignTaskVo signTaskVo);

    void subSign(AddSubSignTaskVo signTaskVo);
}
