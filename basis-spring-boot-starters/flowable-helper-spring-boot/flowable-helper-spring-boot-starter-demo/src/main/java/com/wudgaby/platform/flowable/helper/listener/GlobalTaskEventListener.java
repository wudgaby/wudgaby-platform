package com.wudgaby.platform.flowable.helper.listener;

import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.service.FlowableProcessInstanceService;
import com.wudgaby.platform.flowable.helper.vo.AuditNoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableMultiInstanceActivityCancelledEvent;
import org.flowable.engine.delegate.event.FlowableMultiInstanceActivityCompletedEvent;
import org.flowable.engine.delegate.event.FlowableMultiInstanceActivityEvent;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName : GlobalTaskEventListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/27 9:18
 * @Desc :   全局监听器
 */
@Slf4j(topic = "FLOW_LOG")
@Component
public class GlobalTaskEventListener extends AbstractFlowableEngineEventListener {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    protected void taskCreated(FlowableEngineEntityEvent event) {
        org.flowable.common.engine.impl.event.FlowableEntityEventImpl eventImpl = (org.flowable.common.engine.impl.event.FlowableEntityEventImpl)event;
        //得到任务实例
        TaskEntity entity = (TaskEntity)eventImpl.getEntity();
        log.info("{} 任务已创建. procInsId: {} assigness: {} idLinks: {}", entity.getName(), entity.getProcessInstanceId(), entity.getAssignee(), entity.getIdentityLinks());
        notice(event, entity);
    }

    @Override
    protected void taskAssigned(FlowableEngineEntityEvent event) {
        FlowableEntityEventImpl eventImpl = (FlowableEntityEventImpl)event;
        TaskEntity entity = (TaskEntity)eventImpl.getEntity();
        log.info("{} 任务已分配. taskId: {} assigness: {}", entity.getName(), entity.getId(), entity.getAssignee());
    }

    @Override
    protected void taskCompleted(FlowableEngineEntityEvent event) {
        FlowableEntityEventImpl eventImpl = (FlowableEntityEventImpl)event;
        TaskEntity entity = (TaskEntity)eventImpl.getEntity();
        log.info("{} 任务已完成. procInsId: {} taskId: {} assigness: {}", entity.getName(), entity.getProcessInstanceId(), entity.getId(), entity.getAssignee());

        /*log.info("更新业务表状态审核中. {}", entity.getProcessInstanceId());
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(event.getProcessDefinitionId()).singleResult();
        if(FlowConstant.CASE_PROCESS_DEFINITION_KEY.equals(processDefinition.getKey())){
            warnCaseInfoService.update(new WarnCaseInfo().setExamineState(AuditState.AUDIT_ING),
                    Wrappers.<WarnCaseInfo>lambdaUpdate().eq(WarnCaseInfo::getProcInstId, entity.getProcessInstanceId()));
        }else if(FlowConstant.DISPOSE_PROCESS_DEFINITION_KEY.equals(processDefinition.getKey())){
            examineService.update(new Examine().setExamineApproveStatus(AuditState.AUDIT_ING),
                    Wrappers.<Examine>lambdaUpdate().eq(Examine::getProcInstId, entity.getProcessInstanceId()));
        }*/

        notice(event, entity);
    }

    private void notice(FlowableEngineEntityEvent event, TaskEntity entity){
        AuditNoticeVo auditNoticeVo = new AuditNoticeVo();
        auditNoticeVo.setEventType(event.getType());
        auditNoticeVo.setProcInstId(entity.getProcessInstanceId());
        auditNoticeVo.setTaskId(entity.getId());
        auditNoticeVo.setAuditType(String.valueOf(entity.getVariable(FlowConstant.OUTCOME)));
        auditNoticeVo.setAssignee(entity.getAssignee());
        auditNoticeVo.setCandidates(entity.getCandidates());
        auditNoticeVo.setTask(entity);
        auditNoticeVo.setBizKey(String.valueOf(entity.getVariable(FlowConstant.FLOW_BIZKEY_VAR)));
        auditNoticeVo.setStarterId(String.valueOf(entity.getVariable(FlowConstant.FLOW_INITIATOR_VAR)));

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(event.getProcessDefinitionId()).singleResult();
    }

    @Autowired
    private FlowableProcessInstanceService flowableProcessInstanceService;

    @Override
    protected void multiInstanceActivityCancelled(FlowableMultiInstanceActivityCancelledEvent event) {
        log.info("{} 多实例取消", event.getActivityName());
    }

    @Override
    protected void multiInstanceActivityStarted(FlowableMultiInstanceActivityEvent event) {
        log.info("{} 多实例开始", event.getActivityName());
    }

    @Override
    protected void multiInstanceActivityCompleted(FlowableMultiInstanceActivityCompletedEvent event) {
        log.info("{} 多实例完成", event.getActivityName());
    }

    @Override
    protected void multiInstanceActivityCompletedWithCondition(FlowableMultiInstanceActivityCompletedEvent event) {
        log.info("{} 多实例成功", event.getActivityName());
    }
}
