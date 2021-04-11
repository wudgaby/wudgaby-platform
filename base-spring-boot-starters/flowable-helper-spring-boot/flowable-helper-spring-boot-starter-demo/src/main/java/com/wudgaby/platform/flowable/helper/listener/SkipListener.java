package com.wudgaby.platform.flowable.helper.listener;

import com.wudgaby.platform.flowable.helper.entity.FlowConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName : SkipRoleTaskListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/22 19:19
 * @Desc :   TODO
 */
@Slf4j(topic = "FLOW_LOG")
@Component("skipListener")
public class SkipListener implements ExecutionListener{

    @Override
    public void notify(DelegateExecution execution) {
        FlowConfig flowConfig = execution.getVariable("flowConfig", FlowConfig.class);

        if(execution.getCurrentFlowElement() instanceof UserTask){
            UserTask userTask = ((UserTask)execution.getCurrentFlowElement());
            log.info("是否跳过.{}, {}, {}", userTask.getAssignee(), userTask.getCandidateUsers(), userTask.getCandidateGroups());
            if(StringUtils.isBlank(userTask.getAssignee())
                    && CollectionUtils.isEmpty(userTask.getCandidateUsers())
                    && CollectionUtils.isEmpty(userTask.getCandidateGroups())
                    && MapUtils.isEmpty(userTask.getCustomUserIdentityLinks())
                    && MapUtils.isEmpty(userTask.getCustomGroupIdentityLinks())){

                if(flowConfig.getNobodySkip()){
                    //execution.setVariable(FlowConstant.VAR_TASK_SKIP_KEY, true);
                    userTask.setSkipExpression("${1==1}");
                }else{
                    userTask.setAssignee(flowConfig.getDefaultApprover());
                }
            }
        }
    }
}
