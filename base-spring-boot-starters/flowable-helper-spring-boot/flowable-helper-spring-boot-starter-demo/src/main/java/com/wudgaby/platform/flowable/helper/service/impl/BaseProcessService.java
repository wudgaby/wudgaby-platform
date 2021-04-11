package com.wudgaby.platform.flowable.helper.service.impl;

import cn.hutool.core.lang.UUID;
import com.wudgaby.platform.flowable.helper.mapper.HisFlowableActinstDao;
import com.wudgaby.platform.flowable.helper.mapper.RunFlowableActinstDao;
import com.wudgaby.platform.flowable.helper.service.FlowableCommentService;
import com.wudgaby.platform.flowable.helper.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.*;
import org.flowable.engine.impl.persistence.entity.ActivityInstanceEntity;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : bruce.liu
 * @title: : BaseProcessService
 * @projectName : flowable
 * @description: 基本的流程service
 * @date : 2019/12/220:50
 */
@Slf4j
public abstract class BaseProcessService {
    @Autowired
    protected ManagementService managementService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected FlowableCommentService flowableCommentService;
    @Autowired
    private RunFlowableActinstDao runFlowableActinstDao;
    @Autowired
    private HisFlowableActinstDao hisFlowableActinstDao;

    /**
     * 添加审批意见
     *
     * @param taskId            任务id
     * @param userCode          处理人工号
     * @param processInstanceId 流程实例id
     * @param type              审批类型
     * @param message           审批意见
     */
    protected void addComment(String taskId, String userCode, String processInstanceId, String type, String message) {
        //1.添加备注
        CommentVo commentVo = new CommentVo(taskId, userCode, processInstanceId, type, message);
        flowableCommentService.addComment(commentVo);
        //TODO 2.修改扩展的流程实例表的状态以备查询待办的时候能带出来状态
        //TODO 3.发送mongodb的数据到消息队列里面
    }

    /**
     * 添加审批意见
     *
     * @param userCode          处理人工号
     * @param processInstanceId 流程实例id
     * @param type              审批类型
     * @param message           审批意见
     */
    protected void addComment(String userCode, String processInstanceId, String type, String message) {
        this.addComment(null, userCode, processInstanceId, type, message);
    }

    /**
     * 删除跳转的历史节点信息
     *
     * @param disActivityId     跳转的节点id
     * @param processInstanceId 流程实例id
     */
    protected void deleteActivity(String disActivityId, String processInstanceId) {
        String tableName = managementService.getTableName(ActivityInstanceEntity.class);
        String sql = "select t.* from " + tableName + " t where t.PROC_INST_ID_=#{processInstanceId} and t.ACT_ID_ = #{disActivityId} " +
                " order by t.END_TIME_ ASC";
        List<ActivityInstance> disActivities = runtimeService.createNativeActivityInstanceQuery().sql(sql)
                .parameter("processInstanceId", processInstanceId)
                .parameter("disActivityId", disActivityId).list();
        //删除运行时和历史节点信息
        if (CollectionUtils.isNotEmpty(disActivities)) {
            ActivityInstance activityInstance = disActivities.get(0);
            sql = "select t.* from " + tableName + " t where t.PROC_INST_ID_=#{processInstanceId} and (t.END_TIME_ >= #{endTime} or t.END_TIME_ is null)";
            List<ActivityInstance> datas = runtimeService.createNativeActivityInstanceQuery().sql(sql).parameter("processInstanceId", processInstanceId)
                    .parameter("endTime", activityInstance.getEndTime()).list();
            List<String> runActivityIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(datas)) {
                datas.forEach(ai -> runActivityIds.add(ai.getId()));
                runFlowableActinstDao.deleteRunActinstsByIds(runActivityIds);
                hisFlowableActinstDao.deleteHisActinstsByIds(runActivityIds);
            }
        }
    }

    /**
     * 执行跳转
     */
    protected void moveExecutionsToSingleActivityId(List<String> executionIds, String activityId) {
        runtimeService.createChangeActivityStateBuilder()
                .moveExecutionsToSingleActivityId(executionIds, activityId)
                .changeState();
    }

    protected TaskEntity createSubTask(TaskEntity ptask, String assignee) {
        return this.createSubTask(ptask, ptask.getId(), assignee);
    }

    /**
     * 创建子任务
     *
     * @param ptask    创建子任务
     * @param assignee 子任务的执行人
     * @return
     */
    protected TaskEntity createSubTask(TaskEntity ptask, String ptaskId, String assignee) {
        TaskEntity task = null;
        if (ptask != null) {
            //1.生成子任务
            task = (TaskEntity) taskService.newTask(UUID.fastUUID().toString());
            task.setCategory(ptask.getCategory());
            task.setDescription(ptask.getDescription());
            task.setTenantId(ptask.getTenantId());
            task.setAssignee(assignee);
            task.setName(ptask.getName());
            task.setParentTaskId(ptaskId);
            task.setProcessDefinitionId(ptask.getProcessDefinitionId());
            task.setProcessInstanceId(ptask.getProcessInstanceId());
            task.setTaskDefinitionKey(ptask.getTaskDefinitionKey());
            task.setTaskDefinitionId(ptask.getTaskDefinitionId());
            task.setPriority(ptask.getPriority());
            task.setCreateTime(new Date());
            taskService.saveTask(task);
        }
        return task;
    }

}
