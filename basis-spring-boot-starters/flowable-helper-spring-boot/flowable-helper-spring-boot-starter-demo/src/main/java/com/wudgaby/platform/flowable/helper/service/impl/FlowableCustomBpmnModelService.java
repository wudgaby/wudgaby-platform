package com.wudgaby.platform.flowable.helper.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.consts.FlowableConst;
import com.wudgaby.platform.flowable.helper.vo.flow.FlowConfigVo;
import com.wudgaby.platform.flowable.helper.vo.flow.NodeConfigVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ImplementationType;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName : FlowableBpmnModelService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/27 14:24
 * @Desc :   TODO
 */
@Service
public class FlowableCustomBpmnModelService{
    @Autowired
    private IdmIdentityService idmIdentityService;

    public BpmnModel createCustomBpmnModel(FlowConfigVo flowConfigVo){
        BpmnModel bpmnModel = new BpmnModel();

        //开始节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");

        //开始线
        SequenceFlow startFlow = new SequenceFlow();
        startFlow.setId("startFlow");

        //第一个固定任务
        UserTask initiateUserTask = new UserTask();
        initiateUserTask.setId(FlowConstant.FLOW_INITIATOR_DEF_KEY);

        startFlow.setSourceRef(startEvent.getId());
        startFlow.setTargetRef(initiateUserTask.getId());

        startEvent.setName("开始");
        startEvent.setInitiator(FlowConstant.FLOW_INITIATOR_VAR);
        startEvent.setOutgoingFlows(Lists.newArrayList(startFlow));

        initiateUserTask.setName("发起审批");
        initiateUserTask.setIncomingFlows(Lists.newArrayList(startFlow));

        //支持退回到发起人
        /*if (flowConfigVo.getRejectToInitiator()) {
            initiateUserTask.setSkipExpression("");
            initiateUserTask.setAssignee("${" + FlowConstant.FLOW_INITIATOR_VAR + "}");
        } else {
            initiateUserTask.setSkipExpression("${submitter==''}");
        }*/

        initiateUserTask.setSkipExpression("${submitter==''}");

        //结束线
        SequenceFlow endFlow = new SequenceFlow();
        endFlow.setId("endFlow");

        //结束节点
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");

        endFlow.setTargetRef(endEvent.getId());

        endEvent.setName("结束");
        endEvent.setIncomingFlows(Lists.newArrayList(endFlow));

        //动态节点
        UserTask preUserTask = initiateUserTask;
        List<UserTask> customerUserTaskList = Lists.newArrayList();
        List<SequenceFlow> sequenceFlowList = Lists.newArrayList();

        List<NodeConfigVo> nodeConfigVoList = flowConfigVo.getNodeConfigList();
        int nodeSize = nodeConfigVoList.size();

        for(int i = 0; i < nodeSize; i++){
            int index = i + 1;
            NodeConfigVo nodeConfigVo = nodeConfigVoList.get(i);

            UserTask newUserTask = new UserTask();
            //newUserTask.setId("userTask" + index);
            newUserTask.setId(nodeConfigVo.getNodeKey());
            setAssignee(nodeConfigVo, newUserTask);

            List<FlowableListener> executionListeners = Lists.newArrayList();
            List<FlowableListener> taskListeners = Lists.newArrayList();

            FlowableListener skipListener = new FlowableListener();
            skipListener.setEvent(TaskListener.EVENTNAME_CREATE);
            skipListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
            skipListener.setImplementation("${taskNobodySkipListener}");
            taskListeners.add(skipListener);

            //节点类型
            if(nodeConfigVo.getNodeType() == NodeConfigVo.NodeType.APPROVAL){
                newUserTask.setCategory(FlowableConst.CATEGORY_TODO);
                boolean isMultiUsers = nodeConfigVo.getApportionType() == NodeConfigVo.ApprovalType.USER && CollectionUtils.size(nodeConfigVo.getIdList()) > 1;
                boolean isRole = nodeConfigVo.getApportionType() == NodeConfigVo.ApprovalType.ROLE;
                if (isMultiUsers || isRole) {
                    //https://blog.csdn.net/qq_34758074/article/details/103330904
                    // 配置会签所需条件.
                    MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
                    // 配置节点人员办理顺序 串行:true 并行:false.
                    multiInstanceLoopCharacteristics.setSequential(nodeConfigVo.getSequential());
                    // 运行时 配置会签集合变量名称.
                    multiInstanceLoopCharacteristics.setInputDataItem(nodeConfigVo.getNodeKey() + FlowConstant.VAR_ASSIGNEE_LIST_KEY);

                    //定义时确认会签集合
                    //multiInstanceLoopCharacteristics.setInputDataItem(Joiner.on(",").skipNulls().join(nodeConfigVo.getIdList()));
                    /*List<String> assigneeList = Lists.newArrayList();
                    if(isMultiUsers){
                        assigneeList = nodeConfigVo.getIdList();
                    }
                    //加载角色用户
                    if(isRole){
                        List<User> userList = idmIdentityService.createUserQuery().memberOfGroups(nodeConfigVo.getIdList()).list();
                        if(CollectionUtils.isNotEmpty(userList)){
                            assigneeList = userList.stream().map(User::getId).collect(Collectors.toList());
                        }
                    }
                    multiInstanceLoopCharacteristics.setInputDataItem(Joiner.on(",").skipNulls().join(assigneeList));*/

                    // 配置会签集合遍历名称.
                    multiInstanceLoopCharacteristics.setElementVariable("assignee");
                    multiInstanceLoopCharacteristics.setCompletionCondition("${voteService.isComplete(execution)}");

                    //无用 https://blog.csdn.net/weixin_43697115/article/details/107636407
                    //multiInstanceLoopCharacteristics.setCollectionString("");
                    //loopCardinality和inputDataItem两个必须设置一个，这个在部署流程似有校验
                    //multiInstanceLoopCharacteristics.setLoopCardinality("5");

                    // 设置节点处理人表达式 引用会签条件fowable:elementVariable="assignee".
                    newUserTask.setAssignee("${assignee}");

                    /*if(isMultiUsers){
                        //设置审批人
                        newUserTask.setCandidateUsers(nodeConfigVo.getIdList());
                    }else{
                        //设置审批组
                        newUserTask.setCandidateGroups(nodeConfigVo.getIdList());
                    }*/
                    //newUserTask.setCandidateUsers(null);
                    //newUserTask.setCandidateGroups(null);

                    if(nodeConfigVo.getSignType() == NodeConfigVo.SignType.AND_SIGN){
                        newUserTask.setName(newUserTask.getName() + StrUtil.format("({})", "并签"));
                    }else{
                        newUserTask.setName(newUserTask.getName() + StrUtil.format("({})", "或签"));
                    }

                    // 任务节点设置会签循环特征.
                    newUserTask.setLoopCharacteristics(multiInstanceLoopCharacteristics);

                    FlowableListener flowableListener = new FlowableListener();
                    flowableListener.setEvent(ExecutionListener.EVENTNAME_START);
                    flowableListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
                    flowableListener.setImplementation("${multiInstanceTaskHandler}");
                    executionListeners.add(flowableListener);

                    flowableListener = new FlowableListener();
                    flowableListener.setEvent(ExecutionListener.EVENTNAME_END);
                    flowableListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
                    flowableListener.setImplementation("${multiInstanceCompleteHandler}");
                    executionListeners.add(flowableListener);
                }
            } else if(nodeConfigVo.getNodeType() == NodeConfigVo.NodeType.CC){
                //newUserTask.setCategory(FlowableConst.CATEGORY_TO_READ);
                newUserTask.setCategory(FlowableConst.CC);
                newUserTask.setName("抄送");
                newUserTask.setCandidateUsers(null);
                newUserTask.setCandidateGroups(null);

                //配置前一个任务的抄送人.前一个任务跳过时,无效
                /*Map<String, Set<String>> customUserIdentityLinks = Maps.newIdentityHashMap();
                customUserIdentityLinks.put(FlowableConst.CC, Sets.newHashSet(nodeConfigVo.getIdList()));
                preUserTask.setCustomUserIdentityLinks(customUserIdentityLinks);
                continue;*/

                //不可
                //newUserTask.setSkipExpression("${1==1}");

                Map<String, Set<String>> customUserIdentityLinks = Maps.newIdentityHashMap();
                customUserIdentityLinks.put(FlowableConst.CC, Sets.newHashSet(nodeConfigVo.getIdList()));
                newUserTask.setCustomUserIdentityLinks(customUserIdentityLinks);

                FlowableListener taskListener = new FlowableListener();
                taskListener.setEvent(TaskListener.EVENTNAME_CREATE);
                taskListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
                taskListener.setImplementation("${taskCcCreateEventListener}");
                taskListeners.add(taskListener);
            }

            newUserTask.setExecutionListeners(executionListeners);
            newUserTask.setTaskListeners(taskListeners);
            customerUserTaskList.add(newUserTask);

            SequenceFlow passFlow = new SequenceFlow();
            passFlow.setId("passFlow" + index);
            passFlow.setName("通过");

            SequenceFlow rejectFlow = null;
            boolean notInitTask = !FlowConstant.FLOW_INITIATOR_DEF_KEY.equals(preUserTask.getId());
            if(!flowConfigVo.getOptimized()){
                rejectFlow = new SequenceFlow();
                rejectFlow.setId("rejectFlow" + index);
                if(notInitTask){
                    passFlow.setName("通过");
                    rejectFlow.setName("拒绝");

                    if(preUserTask.hasMultiInstanceLoopCharacteristics()){
                        passFlow.setConditionExpression("${auditService.getVoteResult(execution)}");
                        rejectFlow.setConditionExpression("${!auditService.getVoteResult(execution)}");
                    }else{
                        passFlow.setConditionExpression("${auditService.isApprove(execution)}");
                        rejectFlow.setConditionExpression("${!auditService.isApprove(execution)}");
                    }

                    rejectFlow.setSourceRef(preUserTask.getId());
                    rejectFlow.setTargetRef(endEvent.getId());
                    sequenceFlowList.add(rejectFlow);

                    preUserTask.getOutgoingFlows().add(rejectFlow);
                    endEvent.getIncomingFlows().add(rejectFlow);
                }
            }
            //多实例设定两出口
            /*else if(preUserTask.hasMultiInstanceLoopCharacteristics()){
                rejectFlow = new SequenceFlow();
                rejectFlow.setId("rejectFlow" + index);
                passFlow.setName("通过");
                rejectFlow.setName("拒绝");

                passFlow.setConditionExpression("${auditService.getVoteResult(execution)}");
                rejectFlow.setConditionExpression("${!auditService.getVoteResult(execution)}");

                rejectFlow.setSourceRef(preUserTask.getId());
                rejectFlow.setTargetRef(endEvent.getId());
                sequenceFlowList.add(rejectFlow);

                preUserTask.getOutgoingFlows().add(rejectFlow);
                endEvent.getIncomingFlows().add(rejectFlow);
            }*/

            passFlow.setSourceRef(preUserTask.getId());
            passFlow.setTargetRef(newUserTask.getId());

            preUserTask.setOutgoingFlows(Lists.newArrayList(passFlow));
            newUserTask.setIncomingFlows(Lists.newArrayList(passFlow));
            sequenceFlowList.add(passFlow);

            preUserTask = newUserTask;
        }

        UserTask lastTask = IterableUtils.get(customerUserTaskList, customerUserTaskList.size() - 1);
        endFlow.setName("通过");

        if(!flowConfigVo.getOptimized()){
            boolean notInitTask = !FlowConstant.FLOW_INITIATOR_DEF_KEY.equals(lastTask.getId());
            if(notInitTask){
                endFlow.setConditionExpression("${auditService.isApprove(execution)}");
                if(lastTask.hasMultiInstanceLoopCharacteristics()){
                    endFlow.setConditionExpression("${auditService.getVoteResult(execution)}");
                }
            }
        }
        endFlow.setSourceRef(lastTask.getId());

        //加入流程
        Process mainProcess = new Process();
        mainProcess.setId(flowConfigVo.getDefKey());
        mainProcess.setName(flowConfigVo.getDesc());

        mainProcess.addFlowElement(startEvent);
        mainProcess.addFlowElement(initiateUserTask);
        for(UserTask userTask : customerUserTaskList){
            mainProcess.addFlowElement(userTask);
        }
        mainProcess.addFlowElement(endEvent);

        mainProcess.addFlowElement(startFlow);
        for(SequenceFlow sequenceFlow : sequenceFlowList){
            mainProcess.addFlowElement(sequenceFlow);
        }
        mainProcess.addFlowElement(endFlow);

        bpmnModel.addProcess(mainProcess);

        //这句很关键让程序自动部局各节点的位置，网上很多教程没有提到
        new BpmnAutoLayout(bpmnModel).execute();

        //把BpmnModel转成json格式进行保存
        //BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        //ObjectNode jsonNode = jsonConverter.convertToJson(bpmnModel);
        return bpmnModel;
    }

    private void setAssignee(NodeConfigVo nodeConfigVo, UserTask userTask) {
        if (nodeConfigVo.getApportionType() == NodeConfigVo.ApprovalType.USER) {
            if (CollectionUtils.size(nodeConfigVo.getIdList()) == 1) {
                String userId = IterableUtils.get(nodeConfigVo.getIdList(), 0);
                userTask.setAssignee(userId);

                if (StringUtils.isBlank(nodeConfigVo.getNodeName())) {
                    userTask.setName(IterableUtils.get(nodeConfigVo.getNameList(), 0));
                } else {
                    userTask.setName(nodeConfigVo.getNodeName());
                }

                if (StringUtils.isBlank(userTask.getName())) {
                    /*UserTbl userTbl = userTblService.getById(userId);
                    if (userTbl != null) {
                        userTask.setName(userTbl.getName());
                    }*/
                    User user = idmIdentityService.createUserQuery().userId(userId).singleResult();
                    if(user != null){
                        userTask.setName(user.getDisplayName());
                    }
                }
            } else {
                userTask.setCandidateUsers(nodeConfigVo.getIdList());
                if (StringUtils.isBlank(nodeConfigVo.getNodeName())) {
                    userTask.setName(Joiner.on("/").join(nodeConfigVo.getNameList()));
                } else {
                    userTask.setName(nodeConfigVo.getNodeName());
                }

                if (StringUtils.isBlank(userTask.getName())) {
                    /*List<UserTbl> userTblList = userTblService.list(Wrappers.<UserTbl>lambdaQuery().in(UserTbl::getUserId, bpmnModelVo.getIdList()));
                    if (CollectionUtils.isNotEmpty(userTblList)) {
                        userTask.setName(Joiner.on("/").join(userTblList.stream().map(u -> u.getName()).collect(Collectors.toSet())));
                    }*/
                    List<User> userList = idmIdentityService.createUserQuery().userIds(nodeConfigVo.getIdList()).list();
                    if (CollectionUtils.isNotEmpty(userList)) {
                        userTask.setName(Joiner.on("/").join(userList.stream().map(u -> u.getDisplayName()).collect(Collectors.toSet())));
                    }
                }
            }
        } else if (nodeConfigVo.getApportionType() == NodeConfigVo.ApprovalType.ROLE) {
            userTask.setCandidateGroups(nodeConfigVo.getIdList());

            if (StringUtils.isBlank(nodeConfigVo.getNodeName())) {
                userTask.setName(Joiner.on("/").join(nodeConfigVo.getNameList()));
            } else {
                userTask.setName(nodeConfigVo.getNodeName());
            }

            if (StringUtils.isBlank(userTask.getName())) {
                /*List<RoleTbl> roleTblList = roleTblService.list(Wrappers.<RoleTbl>lambdaQuery().in(RoleTbl::getRoleId, bpmnModelVo.getIdList()));
                if (CollectionUtils.isNotEmpty(roleTblList)) {
                    userTask.setName(Joiner.on("/").join(roleTblList.stream().map(r -> r.getRoleName()).collect(Collectors.toSet())));
                }*/

                List<Group> groupList = idmIdentityService.createGroupQuery().groupIds(nodeConfigVo.getIdList()).list();
                if (CollectionUtils.isNotEmpty(groupList)) {
                    userTask.setName(Joiner.on("/").join(groupList.stream().map(u -> u.getName()).collect(Collectors.toSet())));
                }
            }
        } else if (nodeConfigVo.getApportionType() == NodeConfigVo.ApprovalType.LEAD) {
            userTask.setAssignee(String.format("${empService.getDirectorByLevel(initiator, %d)}", nodeConfigVo.getLevel()));
            //无人处理会卡住
            //userTask.setSkipExpression(String.format("${empService.getDirectorByLevel(initiator, %d) == ''}", nodeConfigVo.getLevel()));

            if (StringUtils.isBlank(nodeConfigVo.getNodeName())) {
                if(nodeConfigVo.getLevel() == 1){
                    userTask.setName("直接领导");
                }else{
                    userTask.setName(nodeConfigVo.getLevel() + "级领导");
                }
            } else {
                userTask.setName(nodeConfigVo.getNodeName());
            }
        } else if (nodeConfigVo.getApportionType() == NodeConfigVo.ApprovalType.EXPR) {
            userTask.setCandidateUsers(Lists.newArrayList(nodeConfigVo.getExpr()));
            //userTask.setSkipExpression(nodeConfigVo.getExpr().replace("}", " == ''}"));
            if (StringUtils.isBlank(nodeConfigVo.getNodeName())) {
                userTask.setName("动态运算");
            } else {
                userTask.setName(nodeConfigVo.getNodeName());
            }
        }
    }
}
