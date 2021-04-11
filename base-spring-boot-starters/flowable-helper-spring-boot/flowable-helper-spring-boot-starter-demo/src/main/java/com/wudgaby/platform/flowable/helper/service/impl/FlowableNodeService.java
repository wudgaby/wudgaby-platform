package com.wudgaby.platform.flowable.helper.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.flowable.helper.cmd.CustomerElCmd;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.service.RoleTblService;
import com.wudgaby.platform.flowable.helper.service.UserTblService;
import com.wudgaby.platform.flowable.helper.vo.IdentityVo;
import com.wudgaby.platform.flowable.helper.vo.UserTaskNodeVo;
import com.wudgaby.redis.api.RedisSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.de.odysseus.el.ExpressionFactoryImpl;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.flowable.common.engine.impl.javax.el.ExpressionFactory;
import org.flowable.common.engine.impl.javax.el.ValueExpression;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : FlowableUtil
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/13 14:56
 * @Desc :   TODO
 */
@Slf4j
@Service
public class FlowableNodeService extends BaseProcessService{
    private static final String PROCESS_NODES_KEY = "caseoa:process:nodes:";
    @Autowired
    private RedisSupport redisSupport;
    @Autowired
    private UserTblService userTblService;
    @Autowired
    private RoleTblService roleTblService;

    /**
     * 动态计算节点信息
     * @param processInsId
     * @return
     */
    public List<UserTaskNodeVo> dynamicsNode(String processInsId){
        List<UserTaskNodeVo> taskNodeVoList = Lists.newArrayList();

        /*Object redisObj = redisSupport.get(PROCESS_NODES_KEY + processInsId);
        if(redisObj != null){
            return (List<UserTaskNodeVo>)redisObj;
        }*/

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInsId).singleResult();
        if(processInstance == null){
            throw new BusinessException("未知的审批流程.");
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<Process> processList = bpmnModel.getProcesses();

        if (CollectionUtils.isNotEmpty(processList)) {
            for (Process process : processList) {
                //Set<FlowElement> sortFlowElementSet = FlowUtils.sortFlowElementSet(process);

                //只处理用户节点
                Collection<UserTask> userTaskList = process.findFlowElementsOfType(UserTask.class);
                if (CollectionUtils.isNotEmpty(userTaskList)) {
                    for (UserTask userTask : userTaskList) {
                        List<IdentityVo> nameList = resolverParam(processInstance, userTask);

                        //过滤跳过的节点
                        if (CollectionUtils.isEmpty(nameList)) {
                            continue;
                        }

                        String desc = null;
                        /*MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
                        if(loopCharacteristics != null){
                            desc = loopCharacteristics.isSequential() ? "(顺序审核)" : "(并行审核)";
                        }else if(CollectionUtils.size(nameList) >= 2){
                            desc = "(任一审核)";
                        }*/

                        taskNodeVoList.add(new UserTaskNodeVo()
                                .setNodeId(userTask.getId())
                                .setNodeName(userTask.getName())
                                .setApproverList(nameList)
                                .setDesc(desc)
                        );
                    }
                }
            }
        }

        //有信息放1天, 无信息放5分. 防止穿透
        /*if(CollectionUtils.isNotEmpty(taskNodeVoList)){
            redisSupport.set(PROCESS_NODES_KEY + processInsId, taskNodeVoList, TimeUnit.DAYS.toSeconds(5));
        }else{
            redisSupport.set(PROCESS_NODES_KEY + processInsId, taskNodeVoList, TimeUnit.MINUTES.toSeconds(5));
        }*/
        return taskNodeVoList;
    }

    private List<IdentityVo> resolverParam(ProcessInstance processInstance, UserTask userTask){
        List<HistoricVariableInstance> hvis = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .list();
        List<IdentityVo> identityVoList = Lists.newArrayList();

        //跳过表达式
        String skipExpression = userTask.getSkipExpression();
        if(StringUtils.isNotBlank(skipExpression)){
            try{
                Boolean skipExpressionResult = managementService.executeCommand(new CustomerElCmd<>(skipExpression, hvis));
                log.info("skipExpression: {} - skipExpressionResult: {}", skipExpression, skipExpressionResult);
                if(skipExpressionResult){
                    return identityVoList;
                }
            }catch (Exception ex){
                log.error("解析uel异常. {}", ex.getMessage());
            }
        }

        boolean isMulti = userTask.hasMultiInstanceLoopCharacteristics();

        //是否是提交任务
        boolean isSubmitTask = userTask.getName().equals(FlowConstant.FLOW_SUBMITTER) ||
                userTask.getId().equals(FlowConstant.FLOW_USER_TASK_SUBMITTER_KEY);

        String assigness = userTask.getAssignee();
        if(StringUtils.isNotBlank(userTask.getAssignee()) && !isMulti){
            assigness = managementService.executeCommand(new CustomerElCmd<>(userTask.getAssignee(), hvis));
        }
        if (StringUtils.isNotBlank(assigness) || isSubmitTask) {
            if(!isMulti) {
                if (isSubmitTask) {
                    assigness = processInstance.getStartUserId();
                }

                if(StringUtils.isNotBlank(assigness)) {
                    User user = identityService.createUserQuery().userId(assigness).singleResult();
                    if (user != null) {
                        identityVoList.add(new IdentityVo(user.getId(), user.getFirstName(), IdentityVo.IdentityType.USER));
                    }
                }
            }else {
                //处理多实例的显示
                MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
                String inputDataItem = loopCharacteristics.getInputDataItem();
                inputDataItem = "${" + inputDataItem + "}";
                Object result = managementService.executeCommand(new CustomerElCmd<>(inputDataItem, hvis));
                List<String> values;
                if(result instanceof Collection){
                    values = (List<String>) result;
                }else{
                    values = Lists.newArrayList(Objects.toString(result));
                }

                if(CollectionUtils.isNotEmpty(values)){
                    List<User> userList = identityService.createUserQuery().userIds(values).list();
                    if (CollectionUtils.isNotEmpty(userList)) {
                        userList.stream().forEach(u -> identityVoList.add(new IdentityVo(u.getId(),u.getFirstName(), IdentityVo.IdentityType.USER)));
                    }
                }
            }
        } else {
            //候选人
            if (CollectionUtils.isNotEmpty(userTask.getCandidateUsers())) {
                List<String> userIds = Lists.newArrayList();
                for(String u : userTask.getCandidateUsers()){
                    Object exprVal = managementService.executeCommand(new CustomerElCmd<>(u, hvis));
                    if(exprVal instanceof Collection){
                        userIds.addAll((List)exprVal);
                    }else{
                        userIds.add(Objects.toString(exprVal));
                    }
                }

                if(CollectionUtils.isNotEmpty(userIds)){
                    List<User> userList = identityService.createUserQuery().userIds(userIds).list();
                    if (CollectionUtils.isNotEmpty(userList)) {
                        userList.stream().forEach(u -> identityVoList.add(new IdentityVo(u.getId(),u.getFirstName(), IdentityVo.IdentityType.USER)));
                    }
                }
            }

            //候选组
            if (CollectionUtils.isNotEmpty(userTask.getCandidateGroups())) {
                List<String> groupIds = Lists.newArrayList();
                for(String g : userTask.getCandidateGroups()){
                    Object exprVal = managementService.executeCommand(new CustomerElCmd<>(g, hvis));
                    if(exprVal instanceof List){
                        groupIds.addAll((List)exprVal);
                    }else{
                        groupIds.add(Objects.toString(exprVal));
                    }
                }

                if(CollectionUtils.isNotEmpty(groupIds)){
                    List<Group> groupList = identityService.createGroupQuery().groupIds(groupIds).list();
                    if (CollectionUtils.isNotEmpty(groupList)) {
                        groupList.stream().forEach(g -> identityVoList.add(new IdentityVo(g.getId(),g.getName(), IdentityVo.IdentityType.GROUP)));
                    }
                }
            }

            if (MapUtils.isNotEmpty(userTask.getCustomUserIdentityLinks())) {
                Set<String> idSet = Sets.newHashSet();
                for(Set<String> s : userTask.getCustomUserIdentityLinks().values()){
                    idSet.addAll(s);
                }

                if(CollectionUtils.isNotEmpty(idSet)){
                    List<User> userList = identityService.createUserQuery().userIds(Lists.newArrayList(idSet)).list();
                    if (CollectionUtils.isNotEmpty(userList)) {
                        userList.stream().forEach(u -> identityVoList.add(new IdentityVo(u.getId(),u.getFirstName(), IdentityVo.IdentityType.USER)));
                    }
                }
            }
        }
        log.info("name: {}, list: {}", userTask.getName(), identityVoList);
        return identityVoList;
    }


    /**
     * 动态计算节点信息
     * @param processDef
     * @return
     */
    public List<UserTaskNodeVo> dynamicsNodeByProcDef(String processDef){
        List<UserTaskNodeVo> taskNodeVoList = Lists.newArrayList();

        Object redisObj = redisSupport.get(PROCESS_NODES_KEY + processDef);
        if(redisObj != null){
            return (List<UserTaskNodeVo>)redisObj;
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDef);
        List<Process> processList = bpmnModel.getProcesses();

        if (CollectionUtils.isNotEmpty(processList)) {
            for (Process process : processList) {
                //只处理用户节点
                Collection<UserTask> userTaskList = process.findFlowElementsOfType(UserTask.class);
                if (CollectionUtils.isNotEmpty(userTaskList)) {
                    for (UserTask userTask : userTaskList) {
                        List<IdentityVo> nameList = resolverParam(userTask);

                        String desc = "";
                        MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
                        if(loopCharacteristics != null){
                            desc = loopCharacteristics.isSequential() ? "(顺序审核)" : "(并行审核)";
                        }else if(CollectionUtils.size(nameList) >= 2){
                            desc = "(任一审核)";
                        }

                        taskNodeVoList.add(new UserTaskNodeVo()
                                .setNodeId(userTask.getId())
                                .setNodeName(userTask.getName())
                                .setApproverList(nameList)
                                .setDesc(desc)
                        );
                    }
                }
            }
        }

        //有信息放1天, 无信息放5分组. 防止穿透
        if(CollectionUtils.isNotEmpty(taskNodeVoList)){
            redisSupport.set(PROCESS_NODES_KEY + processDef, taskNodeVoList, TimeUnit.DAYS.toSeconds(7));
        }else{
            redisSupport.set(PROCESS_NODES_KEY + processDef, taskNodeVoList, TimeUnit.MINUTES.toSeconds(5));
        }
        return taskNodeVoList;
    }


    private List<IdentityVo> resolverParam(UserTask userTask){
        List<IdentityVo> identityVoList = Lists.newArrayList();

        //是否是提交任务
        boolean isSubmitTask = userTask.getName().equals(FlowConstant.FLOW_SUBMITTER) ||
                userTask.getId().equals(FlowConstant.FLOW_USER_TASK_SUBMITTER_KEY);
        if (StringUtils.isNotBlank(userTask.getAssignee()) || isSubmitTask) {
            //处理多实例的显示
            MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
            if(loopCharacteristics == null) {
                if (!isSubmitTask) {
                    try{
                        String assignee = managementService.executeCommand(new CustomerElCmd<>(userTask.getAssignee(), null));
                        User user = identityService.createUserQuery().userId(assignee).singleResult();
                        identityVoList.add(new IdentityVo(user.getId(), user.getFirstName(), IdentityVo.IdentityType.USER));
                    }catch (Exception ex){
                        log.error("解析uel异常. {}", ex.getMessage());
                    }
                }
            }
        } else {
            //候选人
            if (CollectionUtils.isNotEmpty(userTask.getCandidateUsers())) {
                List<String> userIds = Lists.newArrayList();
                for(String u : userTask.getCandidateUsers()){
                    try{
                        String userId = managementService.executeCommand(new CustomerElCmd<>(u, null));
                        userIds.add(userId);
                    }catch (Exception ex){
                        log.error("解析uel异常. {}", ex.getMessage());
                    }
                }

                List<User> userList = identityService.createUserQuery().userIds(userIds).list();
                if (CollectionUtils.isNotEmpty(userList)) {
                    userList.stream().forEach(u -> identityVoList.add(new IdentityVo(u.getId(),u.getFirstName(), IdentityVo.IdentityType.USER)));
                }
            }

            //候选组
            if (CollectionUtils.isNotEmpty(userTask.getCandidateGroups())) {
                List<String> groupIds = Lists.newArrayList();
                for(String g : userTask.getCandidateGroups()){
                    Object exprVal = managementService.executeCommand(new CustomerElCmd<>(g, null));
                    if(exprVal instanceof List){
                        groupIds.addAll((List)exprVal);
                    }else{
                        groupIds.add(Objects.toString(exprVal));
                    }
                }
                List<Group> groupList = identityService.createGroupQuery().groupIds(groupIds).list();
                if (CollectionUtils.isNotEmpty(groupList)) {
                    groupList.stream().forEach(g -> identityVoList.add(new IdentityVo(g.getId(),g.getName(), IdentityVo.IdentityType.GROUP)));
                }
            }
        }
        log.info("name: {}, list: {}", userTask.getName(), identityVoList);
        return identityVoList;
    }


    private Object getValue(List<HistoricVariableInstance> hvis, String exp, Class<?> clazz) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        for (HistoricVariableInstance entry : hvis) {
            context.setVariable(entry.getVariableName(), factory.createValueExpression(entry.getValue(), Object.class));
        }
        ValueExpression e = factory.createValueExpression(context, exp, clazz);
        return e.getValue(context);
    }

    private String getValue(List<HistoricVariableInstance> hvis, String exp) {
        return (String) getValue(hvis, exp, String.class);
    }

    /*private String getCustomerValue(List<HistoricVariableInstance> hvis, String exp) {
        CommandContext commandContext = CommandContextUtil.getCommandContext();
        ProcessEngineConfigurationImpl processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration(commandContext);
        ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();

        Expression expr = expressionManager.createExpression(exp);
        ExecutionEntity variableScope = new ExecutionEntityImpl();
        for (HistoricVariableInstance entry : hvis) {
            variableScope.setVariable(entry.getVariableName(), entry.getValue());
        }
        return (String) expr.getValue(variableScope);
    }*/
}
