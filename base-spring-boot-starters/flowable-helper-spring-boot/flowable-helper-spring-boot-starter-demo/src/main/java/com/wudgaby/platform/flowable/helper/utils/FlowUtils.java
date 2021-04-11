package com.wudgaby.platform.flowable.helper.utils;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.CallActivity;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Gateway;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.ReceiveTask;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.SubProcess;
import org.flowable.bpmn.model.Task;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.el.ExpressionManager;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.repository.ProcessDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2019/6/25
 * Time: 9:41 AM
 * Email: old_camel@163.com
 */
@UtilityClass
public class FlowUtils {
    /**
     * 排序
     * @param process
     * @return
     */
    public static Set<FlowElement> sortFlowElementSet(Process process){
        Set<FlowElement> sortFlowElementSet = Sets.newLinkedHashSet();

        Collection<FlowElement> flowElements = process.getFlowElements();
        Map<String, FlowElement> flowElementMap = flowElements.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

        Collection<UserTask> userTaskList = process.findFlowElementsOfType(UserTask.class);
        Map<String, FlowElement> userTaskMap = userTaskList.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

        StartEvent startEvent = process.findFlowElementsOfType(StartEvent.class).get(0);
        FlowElement findFlowElement = startEvent;
        sortFlowElementSet.add(startEvent);

        while(true){
            List<FlowElement> nextFlowElementList = getNextFlowNode(findFlowElement);

            for(FlowElement nextElement : nextFlowElementList){
                if(!(nextElement instanceof EndEvent)){
                    findFlowElement = nextElement;
                    sortFlowElementSet.add(nextElement);
                }
            }

            FlowElement firstFlowElement = CollUtil.getFirst(nextFlowElementList);
            if(nextFlowElementList.size() == 1 && (firstFlowElement instanceof EndEvent)){
                sortFlowElementSet.add(firstFlowElement);
                break;
            }

            /*if(nextFlowElementList.size() == 1){
                findFlowElement = firstFlowElement;
            }
            if(CollectionUtils.isEmpty(nextFlowElementList)){
                sortFlowElementSet.add(findFlowElement);
                break;
            }*/
        }
        return sortFlowElementSet;
    }

    private static List<FlowElement> getNextFlowNode(FlowElement flowElement){
        FlowNode flowNode = (FlowNode)flowElement;
        List<FlowElement> flowElementList = Lists.newArrayList();
        List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
        for(SequenceFlow sequenceFlow : sequenceFlows){
            flowElementList.add(sequenceFlow.getTargetFlowElement());
        }
        return flowElementList;
    }

    /**
     * 获取下一步骤的用户任务
     *
     * @param repositoryService
     * @param taskService
     * @param map
     * @return
     */
    public static List<UserTask> getNextUserTasks(RepositoryService repositoryService, TaskService taskService, org.flowable.task.api.Task task, Map<String, Object> map) {
        List<UserTask> data = new ArrayList<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        Process mainProcess = bpmnModel.getMainProcess();
        Collection<FlowElement> flowElements = mainProcess.getFlowElements();
        String key = task.getTaskDefinitionKey();
        FlowElement flowElement = bpmnModel.getFlowElement(key);
        next(flowElements, flowElement, map, data);
        return data;
    }

    private static void next(Collection<FlowElement> flowElements, FlowElement flowElement, Map<String, Object> map, List<UserTask> nextUser) {
        //如果是结束节点
        if (flowElement instanceof EndEvent) {
            //如果是子任务的结束节点
            if (getSubProcess(flowElements, flowElement) != null) {
                flowElement = getSubProcess(flowElements, flowElement);
            }
        }
        //获取Task的出线信息--可以拥有多个
        List<SequenceFlow> outGoingFlows = null;
        if (flowElement instanceof Task) {
            outGoingFlows = ((Task) flowElement).getOutgoingFlows();
        } else if (flowElement instanceof Gateway) {
            outGoingFlows = ((Gateway) flowElement).getOutgoingFlows();
        } else if (flowElement instanceof StartEvent) {
            outGoingFlows = ((StartEvent) flowElement).getOutgoingFlows();
        } else if (flowElement instanceof SubProcess) {
            outGoingFlows = ((SubProcess) flowElement).getOutgoingFlows();
        } else if (flowElement instanceof CallActivity) {
            outGoingFlows = ((CallActivity) flowElement).getOutgoingFlows();
        }
        if (outGoingFlows != null && outGoingFlows.size() > 0) {
            //遍历所有的出线--找到可以正确执行的那一条
            for (SequenceFlow sequenceFlow : outGoingFlows) {
                //1.有表达式，且为true
                //2.无表达式
                String expression = sequenceFlow.getConditionExpression();
                if (expression == null ||
                        Boolean.valueOf(
                                String.valueOf(
                                        result(map, expression.substring(expression.lastIndexOf("{") + 1, expression.lastIndexOf("}")))))) {
                    //出线的下一节点
                    String nextFlowElementID = sequenceFlow.getTargetRef();
                    if (checkSubProcess(nextFlowElementID, flowElements, nextUser)) {
                        continue;
                    }

                    //查询下一节点的信息
                    FlowElement nextFlowElement = getFlowElementById(nextFlowElementID, flowElements);
                    //调用流程
                    if (nextFlowElement instanceof CallActivity) {
                        CallActivity ca = (CallActivity) nextFlowElement;
                        if (ca.getLoopCharacteristics() != null) {
                            UserTask userTask = new UserTask();
                            userTask.setId(ca.getId());

                            userTask.setId(ca.getId());
                            userTask.setLoopCharacteristics(ca.getLoopCharacteristics());
                            userTask.setName(ca.getName());
                            nextUser.add(userTask);
                        }
                        next(flowElements, nextFlowElement, map, nextUser);
                    }
                    //用户任务
                    if (nextFlowElement instanceof UserTask) {
                        nextUser.add((UserTask) nextFlowElement);
                    }
                    //排他网关
                    else if (nextFlowElement instanceof ExclusiveGateway) {
                        next(flowElements, nextFlowElement, map, nextUser);
                    }
                    //并行网关
                    else if (nextFlowElement instanceof ParallelGateway) {
                        next(flowElements, nextFlowElement, map, nextUser);
                    }
                    //接收任务
                    else if (nextFlowElement instanceof ReceiveTask) {
                        next(flowElements, nextFlowElement, map, nextUser);
                    }
                    //服务任务
                    else if (nextFlowElement instanceof ServiceTask) {
                        next(flowElements, nextFlowElement, map, nextUser);
                    }
                    //子任务的起点
                    else if (nextFlowElement instanceof StartEvent) {
                        next(flowElements, nextFlowElement, map, nextUser);
                    }
                    //结束节点
                    else if (nextFlowElement instanceof EndEvent) {
                        next(flowElements, nextFlowElement, map, nextUser);
                    }
                }
            }
        }
    }

    /**
     * 判断是否是多实例子流程并且需要设置集合类型变量
     */
    private static boolean checkSubProcess(String Id, Collection<FlowElement> flowElements, List<UserTask> nextUser) {
        for (FlowElement flowElement1 : flowElements) {
            if (flowElement1 instanceof SubProcess && flowElement1.getId().equals(Id)) {

                SubProcess sp = (SubProcess) flowElement1;
                if (sp.getLoopCharacteristics() != null) {
                    String inputDataItem = sp.getLoopCharacteristics().getInputDataItem();
                    UserTask userTask = new UserTask();
                    userTask.setId(sp.getId());
                    userTask.setLoopCharacteristics(sp.getLoopCharacteristics());
                    userTask.setName(sp.getName());
                    nextUser.add(userTask);
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * 查询一个节点的是否子任务中的节点，如果是，返回子任务
     *
     * @param flowElements 全流程的节点集合
     * @param flowElement  当前节点
     * @return
     */
    private static FlowElement getSubProcess(Collection<FlowElement> flowElements, FlowElement flowElement) {
        for (FlowElement flowElement1 : flowElements) {
            if (flowElement1 instanceof SubProcess) {
                for (FlowElement flowElement2 : ((SubProcess) flowElement1).getFlowElements()) {
                    if (flowElement.equals(flowElement2)) {
                        return flowElement1;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 根据ID查询流程节点对象, 如果是子任务，则返回子任务的开始节点
     *
     * @param Id           节点ID
     * @param flowElements 流程节点集合
     * @return
     */
    private static FlowElement getFlowElementById(String Id, Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement.getId().equals(Id)) {
                //如果是子任务，则查询出子任务的开始节点
                if (flowElement instanceof SubProcess) {
                    return getStartFlowElement(((SubProcess) flowElement).getFlowElements());
                }
                return flowElement;
            }
            if (flowElement instanceof SubProcess) {
                FlowElement flowElement1 = getFlowElementById(Id, ((SubProcess) flowElement).getFlowElements());
                if (flowElement1 != null) {
                    return flowElement1;
                }
            }
        }
        return null;
    }

    /**
     * 返回流程的开始节点
     *
     * @param flowElements 节点集合
     * @description:
     */
    private static FlowElement getStartFlowElement(Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof StartEvent) {
                return flowElement;
            }
        }
        return null;
    }

    /**
     * 校验el表达示例
     *
     * @param map
     * @param expression
     * @return
     */
    private static Object result(Map<String, Object> map, String expression) {
        FelEngine fel = new FelEngineImpl();
        FelContext ctx = fel.getContext();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            ctx.set(entry.getKey(), entry.getValue());
        }
        Object result = fel.eval(expression);
        return result;
    }

    /**
     * 普通节点转会签节点.
     * @param processDefinitionId 流程定义ID.
     * @param taskId 当前节点ID.
     * @return: void
     */
    public static void toCountersign(TaskService taskService, RepositoryService repositoryService, String processDefinitionId, String taskId) {
        if (StringUtils.isBlank(processDefinitionId) || StringUtils.isBlank(taskId)) {
            return;
        }

        // 根据任务ID获取当前任务对象.
        org.flowable.task.api.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == task) {
            return;
        }

        // 根据流程定义ID获取流程bpmnModel.
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 获取当前流程对象.
        Process process = bpmnModel.getProcesses().get(0);
        // 根据当前节点ID获取对应任务节点对象.
        UserTask currenUserTask = (UserTask) process.getFlowElement(task.getTaskDefinitionKey());
        // 获取当前节点出线信息.
        SequenceFlow sequenceFlow = currenUserTask.getOutgoingFlows().get(0);
        // 根据当前节点出线信息获取下一节点元素.
        FlowElement flowElement = process.getFlowElement(sequenceFlow.getTargetRef());
        // 判断下一节点元素是否为任务节点.
        if (!(flowElement instanceof UserTask)) {
            return;
        }

        // 配置会签所需条件.
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        // 配置节点人员办理顺序 串行:true 并行:false.
        multiInstanceLoopCharacteristics.setSequential(false);
        // 配置会签集合变量名称.
        multiInstanceLoopCharacteristics.setInputDataItem("${assigneeList}");
        // 配置会签集合遍历名称.
        multiInstanceLoopCharacteristics.setElementVariable("assignee");
        // 将下一节点元素转换为任务节点对象.
        UserTask nextUserTask = (UserTask) flowElement;
        // 设置下一节点处理人表达式 引用会签条件activiti:elementVariable="assignee".
        nextUserTask.setAssignee("${assignee}");
        // 下一任务节点设置会签循环特征.
        nextUserTask.setLoopCharacteristics(multiInstanceLoopCharacteristics);

        // 获取流程引擎配置实现类.
        ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngines.getDefaultProcessEngine().getProcessEngineConfiguration();
        // 创建新的任务实例.
        UserTaskActivityBehavior userTaskActivityBehavior = processEngineConfiguration.getActivityBehaviorFactory().createUserTaskActivityBehavior(nextUserTask);
        // 创建BPMN 2.0规范中描述的多实例功能.
        ParallelMultiInstanceBehavior behavior = new ParallelMultiInstanceBehavior(nextUserTask, userTaskActivityBehavior);
        // 设置下一节点多实例行为.
        nextUserTask.setBehavior(behavior);
        // 设置多实例元素变量.
        behavior.setCollectionElementVariable("assignee");
        // 注入表达式.
        ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();
        // 设置多实例集合表达式.
        behavior.setCollectionExpression(expressionManager.createExpression("${assigneeList}"));
    }
}
