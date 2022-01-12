package com.wudgaby.platform.flowable.helper.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.model.form.PageForm;
import com.wudgaby.platform.flowable.helper.cmd.AddCcIdentityLinkCmd;
import com.wudgaby.platform.flowable.helper.cmd.BackUserTaskCmd;
import com.wudgaby.platform.flowable.helper.cmd.CompleteTaskReadCmd;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.consts.SysConstant;
import com.wudgaby.platform.flowable.helper.entity.UserTbl;
import com.wudgaby.platform.flowable.helper.enums.AuditType;
import com.wudgaby.platform.flowable.helper.enums.CommentTypeEnum;
import com.wudgaby.platform.flowable.helper.form.RejectForm;
import com.wudgaby.platform.flowable.helper.form.TaskCompleteForm;
import com.wudgaby.platform.flowable.helper.mapper.FlowableTaskDao;
import com.wudgaby.platform.flowable.helper.service.FlowableBpmnModelService;
import com.wudgaby.platform.flowable.helper.service.FlowableProcessInstanceService;
import com.wudgaby.platform.flowable.helper.service.FlowableTaskService;
import com.wudgaby.platform.flowable.helper.utils.FlowableUtils;
import com.wudgaby.platform.flowable.helper.vo.AddSignTaskVo;
import com.wudgaby.platform.flowable.helper.vo.AddSubSignTaskVo;
import com.wudgaby.platform.flowable.helper.vo.BackTaskVo;
import com.wudgaby.platform.flowable.helper.vo.ClaimTaskVo;
import com.wudgaby.platform.flowable.helper.vo.CompleteTaskVo;
import com.wudgaby.platform.flowable.helper.vo.DelegateTaskVo;
import com.wudgaby.platform.flowable.helper.vo.EndProcessVo;
import com.wudgaby.platform.flowable.helper.vo.FlowNodeVo;
import com.wudgaby.platform.flowable.helper.vo.IdentityVo;
import com.wudgaby.platform.flowable.helper.vo.ReadTaskVo;
import com.wudgaby.platform.flowable.helper.vo.TaskQueryVo;
import com.wudgaby.platform.flowable.helper.vo.TaskVo;
import com.wudgaby.platform.flowable.helper.vo.TurnTaskVo;
import com.wudgaby.platform.flowable.helper.vo.UserTaskNodeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.idm.api.User;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author : bruce.liu
 * @title: : FlowableTaskServiceImpl
 * @projectName : flowable
 * @description: task service
 * @date : 2019/11/1315:15
 */
@Slf4j
@Service
public class FlowableTaskServiceImpl extends BaseProcessService implements FlowableTaskService {
    private static final Logger flowLogger = LoggerFactory.getLogger("FLOW_LOG");
    @Autowired
    private FlowableTaskDao flowableTaskDao;
    @Autowired
    private FlowableBpmnModelService flowableBpmnModelService;
    @Autowired
    private FlowableTaskService flowableTaskService;
    @Autowired
    private FlowableProcessInstanceService flowableProcessInstanceService;
    @Autowired
    private FlowableNodeService flowableNodeService;

    @Override
    public boolean checkParallelgatewayNode(String taskId) {
        boolean flag = false;
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String executionId = task.getExecutionId();
        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        String pExecutionId = execution.getParentId();
        Execution pExecution = runtimeService.createExecutionQuery().executionId(pExecutionId).singleResult();
        if (pExecution != null) {
            String ppExecutionId = pExecution.getParentId();
            long count = runtimeService.createExecutionQuery().executionId(ppExecutionId).count();
            if (count == 0) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public void backToStepTask(BackTaskVo backTaskVo) {
        TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(backTaskVo.getTaskId()).singleResult();
        //1.把当前的节点设置为空
        if (taskEntity == null) {
            throw new BusinessException("不存在任务实例,请确认!");
        }

        //2.设置审批人
        taskEntity.setAssignee(backTaskVo.getUserCode());
        taskService.saveTask(taskEntity);
        //3.添加驳回意见
        this.addComment(backTaskVo.getTaskId(), backTaskVo.getUserCode(), backTaskVo.getProcessInstanceId(),
                CommentTypeEnum.BH.toString(), backTaskVo.getMessage());
        //4.处理提交人节点
        FlowNode distActivity = flowableBpmnModelService.findFlowNodeByActivityId(taskEntity.getProcessDefinitionId(), backTaskVo.getDistFlowElementId());
        if (distActivity != null) {
            if (FlowConstant.FLOW_SUBMITTER.equals(distActivity.getName())) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(taskEntity.getProcessInstanceId()).singleResult();
                runtimeService.setVariable(backTaskVo.getProcessInstanceId(), FlowConstant.FLOW_SUBMITTER_VAR, processInstance.getStartUserId());
            }
        }
        //5.删除节点
        this.deleteActivity(backTaskVo.getDistFlowElementId(), taskEntity.getProcessInstanceId());
        List<String> executionIds = new ArrayList<>();
        //6.判断节点是不是子流程内部的节点
        if (flowableBpmnModelService.checkActivitySubprocessByActivityId(taskEntity.getProcessDefinitionId(),
                backTaskVo.getDistFlowElementId())
                && flowableBpmnModelService.checkActivitySubprocessByActivityId(taskEntity.getProcessDefinitionId(),
                taskEntity.getTaskDefinitionKey())) {
            //6.1 子流程内部驳回
            Execution executionTask = runtimeService.createExecutionQuery().executionId(taskEntity.getExecutionId()).singleResult();
            String parentId = executionTask.getParentId();
            List<Execution> executions = runtimeService.createExecutionQuery().parentId(parentId).list();
            executions.forEach(execution -> executionIds.add(execution.getId()));
            this.moveExecutionsToSingleActivityId(executionIds, backTaskVo.getDistFlowElementId());
        } else {
            //6.2 普通驳回
            List<Execution> executions = runtimeService.createExecutionQuery().parentId(taskEntity.getProcessInstanceId()).list();
            executions.forEach(execution -> executionIds.add(execution.getId()));
            this.moveExecutionsToSingleActivityId(executionIds, backTaskVo.getDistFlowElementId());
        }
    }

    @Override
    public List<FlowNodeVo> getBackNodesByProcessInstanceId(String processInstanceId, String taskId) {
        List<FlowNodeVo> backNods = new ArrayList<>();
        TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        String currActId = taskEntity.getTaskDefinitionKey();
        //获取运行节点表中usertask
        String sql = "select t.* from act_ru_actinst t where t.ACT_TYPE_ = 'userTask' " +
                " and t.PROC_INST_ID_=#{processInstanceId} and t.END_TIME_ is not null ";
        List<ActivityInstance> activityInstances = runtimeService.createNativeActivityInstanceQuery().sql(sql)
                .parameter("processInstanceId", processInstanceId)
                .list();
        //获取运行节点表的parallelGateway节点并出重
        sql = "SELECT t.ID_, t.REV_,t.PROC_DEF_ID_,t.PROC_INST_ID_,t.EXECUTION_ID_,t.ACT_ID_, t.TASK_ID_, t.CALL_PROC_INST_ID_, t.ACT_NAME_, t.ACT_TYPE_, " +
                " t.ASSIGNEE_, t.START_TIME_, max(t.END_TIME_) as END_TIME_, t.DURATION_, t.DELETE_REASON_, t.TENANT_ID_" +
                " FROM  act_ru_actinst t WHERE t.ACT_TYPE_ = 'parallelGateway' AND t.PROC_INST_ID_ = #{processInstanceId} and t.END_TIME_ is not null" +
                " and t.ACT_ID_ <> #{actId} GROUP BY t.act_id_";
        List<ActivityInstance> parallelGatewaies = runtimeService.createNativeActivityInstanceQuery().sql(sql)
                .parameter("processInstanceId", processInstanceId)
                .parameter("actId", currActId)
                .list();
        //排序
        if (CollectionUtils.isNotEmpty(parallelGatewaies)) {
            activityInstances.addAll(parallelGatewaies);
            activityInstances.sort(Comparator.comparing(ActivityInstance::getEndTime));
        }
        //分组节点
        int count = 0;
        Map<ActivityInstance, List<ActivityInstance>> parallelGatewayUserTasks = new HashMap<>();
        List<ActivityInstance> userTasks = new ArrayList<>();
        ActivityInstance currActivityInstance = null;
        for (ActivityInstance activityInstance : activityInstances) {
            if (BpmnXMLConstants.ELEMENT_GATEWAY_PARALLEL.equals(activityInstance.getActivityType())) {
                count++;
                if (count % 2 != 0) {
                    List<ActivityInstance> datas = new ArrayList<>();
                    currActivityInstance = activityInstance;
                    parallelGatewayUserTasks.put(currActivityInstance, datas);
                }
            }
            if (BpmnXMLConstants.ELEMENT_TASK_USER.equals(activityInstance.getActivityType())) {
                if (count % 2 == 0) {
                    userTasks.add(activityInstance);
                } else {
                    if (parallelGatewayUserTasks.containsKey(currActivityInstance)) {
                        parallelGatewayUserTasks.get(currActivityInstance).add(activityInstance);
                    }
                }
            }
        }
        //组装人员名称
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).finished().list();
        Map<String, List<HistoricTaskInstance>> taskInstanceMap = new HashMap<>();
        List<String> userCodes = new ArrayList<>();
        historicTaskInstances.forEach(historicTaskInstance -> {
            userCodes.add(historicTaskInstance.getAssignee());
            String taskDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
            if (taskInstanceMap.containsKey(historicTaskInstance.getTaskDefinitionKey())) {
                taskInstanceMap.get(taskDefinitionKey).add(historicTaskInstance);
            } else {
                List<HistoricTaskInstance> tasks = new ArrayList<>();
                tasks.add(historicTaskInstance);
                taskInstanceMap.put(taskDefinitionKey, tasks);
            }
        });
        //组装usertask的数据
        List<User> userList = identityService.createUserQuery().userIds(userCodes).list();
        Map<String, String> activityIdUserNames = this.getApplyers(processInstanceId, userList, taskInstanceMap);
        if (CollectionUtils.isNotEmpty(userTasks)) {
            userTasks.forEach(activityInstance -> {
                FlowNodeVo node = new FlowNodeVo();
                node.setNodeId(activityInstance.getActivityId());
                node.setNodeName(activityInstance.getActivityName());
                node.setEndTime(activityInstance.getEndTime());
                node.setUserName(activityIdUserNames.get(activityInstance.getActivityId()));
                backNods.add(node);
            });
        }
        //组装会签节点数据
        if (MapUtils.isNotEmpty(taskInstanceMap)) {
            parallelGatewayUserTasks.forEach((activity, activities) -> {
                FlowNodeVo node = new FlowNodeVo();
                node.setNodeId(activity.getActivityId());
                node.setEndTime(activity.getEndTime());
                StringBuffer nodeNames = new StringBuffer("会签:");
                StringBuffer userNames = new StringBuffer("审批人员:");
                if (CollectionUtils.isNotEmpty(activities)) {
                    activities.forEach(activityInstance -> {
                        nodeNames.append(activityInstance.getActivityName()).append(",");
                        userNames.append(activityIdUserNames.get(activityInstance.getActivityId())).append(",");
                    });
                    node.setNodeName(nodeNames.toString());
                    node.setUserName(userNames.toString());
                    backNods.add(node);
                }
            });
        }
        //去重合并
        List<FlowNodeVo> datas = backNods.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(nodeVo -> nodeVo.getNodeId()))), ArrayList::new));

        //排序
        datas.sort(Comparator.comparing(FlowNodeVo::getEndTime));
        return datas;
    }

    private Map<String, String> getApplyers(String processInstanceId, List<User> userList, Map<String, List<HistoricTaskInstance>> taskInstanceMap) {
        Map<String, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        Map<String, String> applyMap = new HashMap<>();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        taskInstanceMap.forEach((activityId, taskInstances) -> {
            StringBuffer applyers = new StringBuffer();
            StringBuffer finalApplyers = applyers;
            taskInstances.forEach(taskInstance -> {
                if (!taskInstance.getName().equals(FlowConstant.FLOW_SUBMITTER)) {
                    User user = userMap.get(taskInstance.getAssignee());
                    if (user != null) {
                        if (StringUtils.indexOf(finalApplyers.toString(), user.getDisplayName()) == -1) {
                            finalApplyers.append(user.getDisplayName()).append(",");
                        }
                    }
                } else {
                    String startUserId = processInstance.getStartUserId();
                    User user = identityService.createUserQuery().userId(startUserId).singleResult();
                    if (user != null) {
                        finalApplyers.append(user.getDisplayName()).append(",");
                    }
                }
            });
            if (applyers.length() > 0) {
                applyers = applyers.deleteCharAt(applyers.length() - 1);
            }
            applyMap.put(activityId, applyers.toString());
        });
        return applyMap;
    }

    @Override
    public void beforeAddSignTask(AddSignTaskVo addSignTaskVo) {
        this.addSignTask(addSignTaskVo, false);
    }

    @Override
    public void afterAddSignTask(AddSignTaskVo addSignTaskVo) {
        this.addSignTask(addSignTaskVo, true);
    }

    @Override
    public void addSignTask(AddSignTaskVo addSignTaskVo, Boolean flag) {
        TaskEntityImpl taskEntity = (TaskEntityImpl) taskService.createTaskQuery().taskId(addSignTaskVo.getTaskId()).singleResult();
        //1.把当前的节点设置为空
        if (taskEntity != null) {
            //如果是加签再加签
            String parentTaskId = taskEntity.getParentTaskId();
            if (StringUtils.isBlank(parentTaskId)) {
                taskEntity.setOwner(addSignTaskVo.getUserCode());
                taskEntity.setAssignee(null);
                taskEntity.setCountEnabled(true);
                if (flag) {
                    taskEntity.setScopeType(FlowConstant.AFTER_ADDSIGN);
                } else {
                    taskEntity.setScopeType(FlowConstant.BEFORE_ADDSIGN);
                }
                //1.2 设置任务为空执行者
                taskService.saveTask(taskEntity);
            }
            //2.添加加签数据
            this.createSignSubTasks(addSignTaskVo, taskEntity);
            //3.添加审批意见
            String type = flag ? CommentTypeEnum.HJQ.toString() : CommentTypeEnum.QJQ.toString();
            this.addComment(addSignTaskVo.getTaskId(), addSignTaskVo.getUserCode(), addSignTaskVo.getProcessInstanceId(),
                    type, addSignTaskVo.getMessage());
            String message = flag ? "后加签成功" : "前加签成功";
            return;
        }
        throw new BusinessException("不存在任务实例，请确认!");
    }

    /**
     * 创建加签子任务
     *
     * @param signVo     加签参数
     * @param taskEntity 父任务
     */
    private void createSignSubTasks(AddSignTaskVo signVo, TaskEntity taskEntity) {
        if (CollectionUtils.isNotEmpty(signVo.getSignPersoneds())) {
            String parentTaskId = taskEntity.getParentTaskId();
            if (StringUtils.isBlank(parentTaskId)) {
                parentTaskId = taskEntity.getId();
            }
            String finalParentTaskId = parentTaskId;
            //1.创建被加签人的任务列表
            signVo.getSignPersoneds().forEach(userCode -> {
                if (StringUtils.isNotBlank(userCode)) {
                    this.createSubTask(taskEntity, finalParentTaskId, userCode);
                }
            });
            String taskId = taskEntity.getId();
            if (StringUtils.isBlank(taskEntity.getParentTaskId())) {
                //2.创建加签人的任务并执行完毕
                Task task = this.createSubTask(taskEntity, finalParentTaskId, signVo.getUserCode());
                taskId = task.getId();
            }
            Task taskInfo = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (null != taskInfo) {
                taskService.complete(taskId);
            }
            //如果是候选人，需要删除运行时候选表种的数据。
            long candidateCount = taskService.createTaskQuery().taskId(parentTaskId).taskCandidateUser(signVo.getUserCode()).count();
            if (candidateCount > 0) {
                taskService.deleteCandidateUser(parentTaskId, signVo.getUserCode());
            }
        }
    }

    @Override
    public void claimTask(ClaimTaskVo claimTaskVo) {
        TaskEntityImpl currTask = (TaskEntityImpl) taskService.createTaskQuery().taskId(claimTaskVo.getTaskId()).singleResult();
        if (currTask == null) {
            throw new BusinessException("签收失败.");
        }
        //1.添加审批意见
        this.addComment(claimTaskVo.getTaskId(), claimTaskVo.getProcessInstanceId(), CommentTypeEnum.QS.toString(), claimTaskVo.getMessage());
        //2.签收
        taskService.claim(claimTaskVo.getTaskId(), claimTaskVo.getUserCode());
    }

    @Override
    public void unClaimTask(ClaimTaskVo claimTaskVo) {
        TaskEntityImpl currTask = (TaskEntityImpl) taskService.createTaskQuery().taskId(claimTaskVo.getTaskId()).singleResult();
        if (currTask == null) {
            throw new BusinessException("取消签收失败.");
        }

        //1.添加审批意见
        this.addComment(claimTaskVo.getTaskId(), claimTaskVo.getProcessInstanceId(), CommentTypeEnum.QXQS.toString(), claimTaskVo.getMessage());
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(claimTaskVo.getTaskId());
        boolean flag = false;
        if (CollectionUtils.isNotEmpty(identityLinks)) {
            for (IdentityLink link : identityLinks) {
                if (IdentityLinkType.CANDIDATE.equals(link.getType())) {
                    flag = true;
                    break;
                }
            }
        }
        //2.反签收
        if (flag) {
            taskService.claim(claimTaskVo.getTaskId(), null);
        } else {
            throw new BusinessException("由于没有候选人或候选组,会导致任务无法认领,请确认.");
        }
    }

    @Override
    public void delegateTask(DelegateTaskVo delegateTaskVo) {
        TaskEntityImpl currTask = (TaskEntityImpl) taskService.createTaskQuery().taskId(delegateTaskVo.getTaskId()).singleResult();
        if (currTask == null) {
            throw new BusinessException("没有运行时的任务实例,请确认!");
        }

        //1.添加审批意见
        this.addComment(delegateTaskVo.getTaskId(), delegateTaskVo.getUserCode(), delegateTaskVo.getProcessInstanceId(), CommentTypeEnum.WP.toString(), delegateTaskVo.getMessage());
        //2.设置审批人就是当前登录人
        taskService.setAssignee(delegateTaskVo.getTaskId(), delegateTaskVo.getUserCode());
        //3.执行委派
        taskService.delegateTask(delegateTaskVo.getTaskId(), delegateTaskVo.getDelegateUserCode());
    }

    @Override
    public void turnTask(TurnTaskVo turnTaskVo) {
        TaskEntityImpl currTask = (TaskEntityImpl) taskService.createTaskQuery().taskId(turnTaskVo.getTaskId()).singleResult();
        if (currTask == null) {
            throw new BusinessException("没有运行时的任务实例,请确认!");
        }

        //1.生成历史记录
        TaskEntity task = this.createSubTask(currTask, turnTaskVo.getUserCode());
        //2.添加审批意见
        this.addComment(task.getId(), turnTaskVo.getUserCode(), turnTaskVo.getProcessInstanceId(), CommentTypeEnum.ZB.toString(), turnTaskVo.getMessage());
        taskService.complete(task.getId());
        //3.转办
        taskService.setAssignee(turnTaskVo.getTaskId(), turnTaskVo.getTurnToUserId());
        taskService.setOwner(turnTaskVo.getTaskId(), turnTaskVo.getUserCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(CompleteTaskVo params) {
        if (StringUtils.isNotBlank(params.getTaskId())) {
            //1.查看当前任务是存在
            TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(params.getTaskId()).singleResult();
            if (taskEntity != null) {
                String taskId = params.getTaskId();
                //2.委派处理
                if (DelegationState.PENDING.equals(taskEntity.getDelegationState())) {
                    //2.1生成历史记录
                    TaskEntity task = this.createSubTask(taskEntity, params.getUserCode());
                    taskService.complete(task.getId());
                    taskId = task.getId();
                    //2.2执行委派
                    taskService.resolveTask(params.getTaskId(), params.getVariables());
                } else {
                    if(StringUtils.isNotBlank(taskEntity.getAssignee()) &&
                            !taskEntity.getAssignee().equals(params.getUserCode())){
                        throw new BusinessException("该任务已被其他用户签收处理.");
                    }

                    //3.1修改执行人 其实我这里就相当于签收了
                    taskService.setAssignee(params.getTaskId(), params.getUserCode());
                    taskService.setVariablesLocal(params.getTaskId(), params.getLocalVariables());
                    //3.2执行任务
                    taskService.complete(params.getTaskId(), params.getVariables(), params.getTransientVariables());
                    //4.处理加签父任务
                    String parentTaskId = taskEntity.getParentTaskId();
                    if (StringUtils.isNotBlank(parentTaskId)) {
                        String tableName = managementService.getTableName(TaskEntity.class);
                        String sql = "select count(1) from " + tableName + " where PARENT_TASK_ID_=#{parentTaskId}";
                        long subTaskCount = taskService.createNativeTaskQuery().sql(sql).parameter("parentTaskId", parentTaskId).count();
                        if (subTaskCount == 0) {
                            Task task = taskService.createTaskQuery().taskId(parentTaskId).singleResult();
                            //处理前后加签的任务
                            taskService.resolveTask(parentTaskId);
                            if (FlowConstant.AFTER_ADDSIGN.equals(task.getScopeType())) {
                                taskService.complete(parentTaskId);
                            }
                        }
                    }
                }
                String type = params.getType() == null ? CommentTypeEnum.SP.toString() : params.getType();
                //5.生成审批意见
                this.addComment(taskId, params.getUserCode(), taskEntity.getProcessInstanceId(), type, params.getMessage());
            } else {
                throw new BusinessException("没有此任务，请确认!");
            }
        } else {
            throw new BusinessException("请输入正确的参数!");
        }
    }

    @Override
    public Task findTaskById(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public Page<TaskVo> getApplyingTasks(TaskQueryVo params, PageForm pageForm) {
        PageHelper.startPage(pageForm.getPageNum().intValue(), pageForm.getPageCount().intValue());
        Page<TaskVo> applyingTasks = flowableTaskDao.getApplyingTasks(params);
        return applyingTasks;
    }

    @Override
    public Page<TaskVo> getApplyedTasks(TaskQueryVo params, PageForm pageForm) {
        PageHelper.startPage(pageForm.getPageNum().intValue(), pageForm.getPageCount().intValue());
        Page<TaskVo> applyedTasks = flowableTaskDao.getApplyedTasks(params);
        return applyedTasks;
    }

    @Override
    public List<User> getApprovers(String processInstanceId) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return getApprover(list, false);
    }

    @Override
    public List<User> getHisApprovers(String processInstanceId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
        return getApprover(list, true);
    }

    @Override
    public List<User> getTaskApprovers(String taskId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskId(taskId).list();
        return getApprover(list, false);
    }

    private List<User> getApprover(List<? extends TaskInfo> taskInfoList, boolean isHisTask){
        List<User> users = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(taskInfoList)) {
            taskInfoList.forEach(task -> {
                if (StringUtils.isNotBlank(task.getAssignee())) {
                    //1.审批人ASSIGNEE_是用户id
                    User user = identityService.createUserQuery().userId(task.getAssignee()).singleResult();
                    if (user != null) {
                        users.add(user);
                    }
                    //2.审批人ASSIGNEE_是组id
                    List<User> gusers = identityService.createUserQuery().memberOfGroup(task.getAssignee()).list();
                    if (CollectionUtils.isNotEmpty(gusers)) {
                        users.addAll(gusers);
                    }
                } else {
                    List<IdentityLinkInfo> linkInfoList = null;
                    if(isHisTask){
                        List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(task.getId());
                        if (CollectionUtils.isNotEmpty(identityLinks)) {
                            linkInfoList = identityLinks.stream().map(identityLink -> (IdentityLinkInfo)identityLink ).collect(Collectors.toList());
                        }
                    }else{
                        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
                        if (CollectionUtils.isNotEmpty(identityLinks)) {
                            linkInfoList = identityLinks.stream().map(identityLink -> (IdentityLinkInfo)identityLink ).collect(Collectors.toList());
                        }
                    }

                    if (CollectionUtils.isNotEmpty(linkInfoList)) {
                        linkInfoList.forEach(identityLink -> {
                            //3.审批人ASSIGNEE_为空,用户id
                            if (StringUtils.isNotBlank(identityLink.getUserId())) {
                                User user = identityService.createUserQuery().userId(identityLink.getUserId()).singleResult();
                                if (user != null) {
                                    users.add(user);
                                }
                            } else {
                                //4.审批人ASSIGNEE_为空,组id
                                List<User> gusers = identityService.createUserQuery().memberOfGroup(identityLink.getGroupId()).list();
                                if (CollectionUtils.isNotEmpty(gusers)) {
                                    users.addAll(gusers);
                                }
                            }
                        });
                    }
                }
            });
        }
        return users;
    }

    @Override
    public Collection<IdentityVo> getApproverAndRoleList() {
        ProcessDefinition caseProcessDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(FlowConstant.CASE_PROCESS_DEFINITION_KEY)
                .latestVersion().singleResult();

        ProcessDefinition disposeProcessDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(FlowConstant.DISPOSE_PROCESS_DEFINITION_KEY)
                .latestVersion().singleResult();

        Set<ProcessDefinition> procDefList = Sets.newHashSet();
        procDefList.add(caseProcessDefinition);
        procDefList.add(disposeProcessDefinition);

        Set<UserTaskNodeVo> userTaskNodeVoList = Sets.newHashSet();

        for(ProcessDefinition processDefinition : procDefList){
            userTaskNodeVoList.addAll(flowableNodeService.dynamicsNodeByProcDef(processDefinition.getId()));
        }

        Set<String> procInsIdList = Sets.newHashSet();
        //所有进行中的任务
        List<Task> taskList = taskService.createTaskQuery().list();
        if (CollectionUtils.isNotEmpty(taskList)) {
            procInsIdList = taskList.stream().map(t->t.getProcessInstanceId()).collect(Collectors.toSet());
        }

        for(String procInsId : procInsIdList){
            userTaskNodeVoList.addAll(flowableNodeService.dynamicsNode(procInsId));
        }

        Set<IdentityVo> approverList = Sets.newHashSet();
        for(UserTaskNodeVo nodeVo : userTaskNodeVoList){
            approverList.addAll(nodeVo.getApproverList());
        }

        return approverList;
    }

    @Override
    public void complete(TaskCompleteForm taskCompleteForm, String userId) {
        UserTbl currentUser = new UserTbl();
        currentUser.setUserId(userId);

        flowLogger.info("{} 完成任务. taskId : {}, auditType: {}", currentUser.getAccount(), taskCompleteForm.getTaskId(), taskCompleteForm.getAuditType());

        CompleteTaskVo completeTaskVo = new CompleteTaskVo();
        completeTaskVo.setTaskId(taskCompleteForm.getTaskId());
        completeTaskVo.setUserCode(currentUser.getUserId());

        Map<String, Object> variables = Maps.newHashMap();
        variables.put(FlowConstant.OUTCOME, taskCompleteForm.getAuditType().name());

        Map<String, Object> localVariables = Maps.newHashMap();

        //当前任务
        TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(completeTaskVo.getTaskId()).singleResult();

        boolean isMultiInstance = false;
        if(taskEntity != null){
            BpmnModel bpmnModel = repositoryService.getBpmnModel(taskEntity.getProcessDefinitionId());
            UserTask usertask = (UserTask)bpmnModel.getFlowElement(taskEntity.getTaskDefinitionKey());
            String nodeId = usertask.getId();
            isMultiInstance = usertask.hasMultiInstanceLoopCharacteristics();
            //是否多实例
            if(isMultiInstance){
                variables.put(nodeId + SysConstant.SYMBOL_UNDERLINE + FlowConstant.VOTE + completeTaskVo.getTaskId(), taskCompleteForm.getAuditType().name());
            }

            // 处理抄送
            if (CollectionUtils.isNotEmpty(taskCompleteForm.getCcToVos())) {
                managementService.executeCommand(new AddCcIdentityLinkCmd(taskEntity.getProcessInstanceId(), taskEntity.getId(),
                        currentUser.getUserId(), taskCompleteForm.getCcToVos()));
            }
        }

        completeTaskVo.setVariables(variables);
        completeTaskVo.setLocalVariables(localVariables);

        String comment = taskCompleteForm.getComment();
        if(taskCompleteForm.getAuditType() == AuditType.pass){
            completeTaskVo.setType(CommentTypeEnum.SP_PASS.toString());
            comment = "";
        }else if(taskCompleteForm.getAuditType() == AuditType.reject){
            completeTaskVo.setType(CommentTypeEnum.SP_REJECT.toString());
            if(StringUtils.isBlank(comment)){
                throw new BusinessException("请填写原因.");
            }
        }else if(taskCompleteForm.getAuditType() == AuditType.abstain){
            completeTaskVo.setType(CommentTypeEnum.SP_ABSTAIN.toString());
            comment = "";
        }else{
            throw new BusinessException("无法处理该审批类型");
        }
        completeTaskVo.setMessage(comment);
        flowableTaskService.complete(completeTaskVo);

        if(isMultiInstance){
            //多实例
            /*Object voteResult= taskEntity.getVariableLocal(FlowConstant.VOTE_RESULT);
            if(voteResult != null && Boolean.getBoolean(voteResult.toString()) == false){
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(taskEntity.getProcessInstanceId()).singleResult();
                if(processInstance != null){
                    runtimeService.deleteProcessInstance(taskEntity.getProcessInstanceId(), "审批拒绝");
                }
            }*/
        }else{
            //审批拒绝,直接结束流程
            if(taskEntity != null && taskCompleteForm.getAuditType() == AuditType.reject){
                /*ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(taskEntity.getProcessInstanceId()).singleResult();
                if(processInstance != null){
                    runtimeService.deleteProcessInstance(taskEntity.getProcessInstanceId(), "审批拒绝");
                }*/
                //终止流程
                EndProcessVo endProcessVo = new EndProcessVo();
                endProcessVo.setMessage("");
                endProcessVo.setProcessInstanceId(taskEntity.getProcessInstanceId());
                endProcessVo.setUserCode(currentUser.getUserId());
                flowableProcessInstanceService.stopProcessInstanceById(endProcessVo);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repeal(RejectForm rejectForm, String userId) {
        UserTbl currentUser = new UserTbl();
        currentUser.setUserId(userId);

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(rejectForm.getProcessInsId()).singleResult();
        if(historicProcessInstance == null){
            throw new BusinessException("该审批流程未找到.");
        }

        if(!historicProcessInstance.getStartUserId().equals(String.valueOf(currentUser.getUserId()))){
            throw new BusinessException("非发起人，不能撤销！.");
        }

        if(historicProcessInstance.getEndTime() != null){
            throw new BusinessException("该审批流程已结束.");
        }
        //撤销流程参数,流程结束时判断结果.
        runtimeService.setVariables(historicProcessInstance.getId(), ImmutableMap.of(FlowConstant.OUTCOME, AuditType.repeal));

        EndProcessVo endProcessVo = new EndProcessVo();
        endProcessVo.setMessage("");
        endProcessVo.setProcessInstanceId(rejectForm.getProcessInsId());
        endProcessVo.setUserCode(currentUser.getUserId());
        flowableProcessInstanceService.stopProcessInstanceById(endProcessVo);

        //this.addComment(currentUser.getUserId(), rejectForm.getProcessInsId(), CommentTypeEnum.CX.getName(), "");
    }

    @Override
    public void stopProcessInstance(EndProcessVo endProcessVo) {
        Task task = taskService.createTaskQuery().taskId(endProcessVo.getTaskId()).singleResult();
        if (task == null) {
            throw new BusinessException("没有运行时的任务实例,请确认!");
        }
        endProcessVo.setProcessInstanceId(task.getProcessInstanceId());
        flowableProcessInstanceService.stopProcessInstanceById(endProcessVo);
    }

    @Override
    public List<FlowNodeVo> getBackNodes(String taskId) {
        TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        if (taskEntity == null) {
            throw new BusinessException("任务不存在");
        }

        String processInstanceId = taskEntity.getProcessInstanceId();
        String currActId = taskEntity.getTaskDefinitionKey();
        String processDefinitionId = taskEntity.getProcessDefinitionId();
        Process process = repositoryService.getBpmnModel(processDefinitionId).getMainProcess();
        FlowNode currentFlowElement = (FlowNode) process.getFlowElement(currActId, true);
        List<ActivityInstance> activitys =
                runtimeService.createActivityInstanceQuery().processInstanceId(processInstanceId).finished().orderByActivityInstanceStartTime().asc().list();
        List<String> activityIds =
                activitys.stream().filter(activity -> activity.getActivityType().equals(BpmnXMLConstants.ELEMENT_TASK_USER)).filter(activity -> !activity.getActivityId().equals(currActId)).map(ActivityInstance::getActivityId).distinct().collect(Collectors.toList());
        List<FlowNodeVo> result = new ArrayList<>();
        for (String activityId : activityIds) {
            FlowNode toBackFlowElement = (FlowNode) process.getFlowElement(activityId, true);
            if (FlowableUtils.isReachable(process, toBackFlowElement, currentFlowElement)) {
                FlowNodeVo vo = new FlowNodeVo();
                vo.setNodeId(activityId);
                vo.setNodeName(toBackFlowElement.getName());
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backTask(BackTaskVo backTaskVo) {
        String taskId = backTaskVo.getTaskId();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String backSysMessage = "退回到" + task.getName() + "。";
        this.addComment(taskId, backTaskVo.getUserCode(), task.getProcessInstanceId(), CommentTypeEnum.TH.name(), backSysMessage + backTaskVo.getMessage());
        String targetRealActivityId = managementService.executeCommand(new BackUserTaskCmd(runtimeService,
                backTaskVo.getTaskId(), backTaskVo.getDistFlowElementId()));

        // 退回发起者处理,退回到发起者,默认设置任务执行人为发起者
        if (FlowConstant.FLOW_INITIATOR_DEF_KEY.equals(targetRealActivityId)) {
            String initiator = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult().getStartUserId();
            List<Task> newTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
            for (Task newTask : newTasks) {
                // 约定：发起者节点
                if (FlowConstant.FLOW_INITIATOR_DEF_KEY.equals(newTask.getTaskDefinitionKey())) {
                    if (ObjectUtils.isEmpty(newTask.getAssignee())) {
                        taskService.setAssignee(newTask.getId(), initiator);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readTask(ReadTaskVo readTaskVo) {
        List<String> taskIds = readTaskVo.getTaskIds();
        String userId = "1";
        for (String taskId : taskIds) {
            managementService.executeCommand(new CompleteTaskReadCmd(taskId, userId));
        }
    }

    @Override
    public void addSign(AddSubSignTaskVo signTaskVo) {
        TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(signTaskVo.getTaskId()).singleResult();
        if (taskEntity == null) {
            throw new BusinessException("任务不存在");
        }
        runtimeService.addMultiInstanceExecution(taskEntity.getTaskDefinitionId(),
                taskEntity.getProcessInstanceId(),
                Collections.singletonMap(taskEntity.getTaskDefinitionId() + FlowConstant.VAR_ASSIGNEE_LIST_KEY, signTaskVo.getUserIds()));
    }

    @Override
    public void subSign(AddSubSignTaskVo signTaskVo) {
        TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(signTaskVo.getTaskId()).singleResult();
        if (taskEntity == null) {
            throw new BusinessException("任务不存在");
        }
        runtimeService.deleteMultiInstanceExecution(taskEntity.getExecutionId(), true);
    }
}
