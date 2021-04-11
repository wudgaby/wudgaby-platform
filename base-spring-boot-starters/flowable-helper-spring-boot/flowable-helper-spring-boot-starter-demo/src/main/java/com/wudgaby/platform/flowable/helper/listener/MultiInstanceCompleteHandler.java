package com.wudgaby.platform.flowable.helper.listener;

import com.wudgaby.platform.flowable.helper.cmd.ActJumpTaskCmd;
import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.service.FlowableBpmnModelService;
import com.wudgaby.platform.flowable.helper.service.FlowableProcessInstanceService;
import com.wudgaby.platform.flowable.helper.service.FlowableTaskService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.engine.ManagementService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j(topic = "FLOW_LOG")
@Component("multiInstanceCompleteHandler")
public class MultiInstanceCompleteHandler implements ExecutionListener{

    @Autowired
    private TaskService taskService;
    @Autowired
    private FlowableProcessInstanceService flowableProcessInstanceService;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private FlowableBpmnModelService flowableBpmnModelService;
    @Autowired
    private FlowableTaskService flowableTaskService;

    @Override
    public void notify(DelegateExecution execution) {
        Boolean voteResult = execution.getVariable(FlowConstant.VOTE_RESULT, Boolean.class);
        if(voteResult != null && voteResult == false){
            //终止流程 无作用
           /* EndProcessVo endProcessVo = new EndProcessVo();
            endProcessVo.setMessage("");
            endProcessVo.setProcessInstanceId(execution.getProcessInstanceId());
            endProcessVo.setUserCode("1");
            flowableProcessInstanceService.stopProcessInstanceById(endProcessVo);*/

            List<EndEvent> endNodes = flowableBpmnModelService.findEndFlowElement(execution.getProcessDefinitionId());
            String endId = endNodes.get(0).getId();
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(execution.getProcessInstanceId())
                    .taskDefinitionKey(execution.getCurrentActivityId())
                    .list();

            managementService.executeCommand(new ActJumpTaskCmd(execution.getProcessInstanceId(),
                    taskList.get(0).getId(),
                    endId, null, ""));
        }
        //execution.removeVariable(FlowConstant.VOTE_RESULT);
    }
}
