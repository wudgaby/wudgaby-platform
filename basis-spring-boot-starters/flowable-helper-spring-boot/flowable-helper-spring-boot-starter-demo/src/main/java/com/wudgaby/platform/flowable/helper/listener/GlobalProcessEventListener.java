package com.wudgaby.platform.flowable.helper.listener;

import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.vo.AuditNoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.delegate.event.FlowableProcessStartedEvent;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName : GlobalProcessEventListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/27 9:18
 * @Desc :   全局监听器
 */
@Slf4j(topic = "FLOW_LOG")
@Component
public class GlobalProcessEventListener extends AbstractFlowableEngineEventListener {
    @Autowired
    private RepositoryService repositoryService;

    @Override
    protected void processStarted(FlowableProcessStartedEvent event) {
        FlowableProcessStartedEventImpl eventImpl = (FlowableProcessStartedEventImpl)event;
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl)eventImpl.getEntity();
        log.info("流程已开始. procInsId: {} - name: {}", eventImpl.getProcessInstanceId(), executionEntity.getName());
    }

    @Override
    protected void processCreated(FlowableEngineEntityEvent event) {
        FlowableEntityEventImpl eventImpl  = (FlowableEntityEventImpl)event;
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl)eventImpl.getEntity();
        log.info("流程已创建. busKey: {}, procInsId: {} - procName: {} - procDefKey:{}", executionEntity.getBusinessKey(), event.getProcessInstanceId(), executionEntity.getName(), executionEntity.getProcessDefinitionKey());

        executionEntity.setVariable(FlowConstant.FLOW_BIZKEY_VAR, executionEntity.getBusinessKey());
        executionEntity.setVariable(FlowConstant.PROCESS_DEF_VAR, executionEntity.getProcessDefinitionKey());
    }

    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        FlowableEntityEventImpl eventImpl  = (FlowableEntityEventImpl)event;
        ExecutionEntityImpl entity = (ExecutionEntityImpl)eventImpl.getEntity();
        log.info("流程已结束. busKey:{}, procId: {} - eventName: {}", entity.getVariable(FlowConstant.FLOW_BIZKEY_VAR), event.getProcessInstanceId(), entity.getName());

        AuditNoticeVo auditNoticeVo = new AuditNoticeVo();
        auditNoticeVo.setEventType(event.getType());
        auditNoticeVo.setProcInstId(entity.getProcessInstanceId());
        auditNoticeVo.setTaskId(entity.getId());
        auditNoticeVo.setAuditType(String.valueOf(entity.getVariable(FlowConstant.OUTCOME)));
        auditNoticeVo.setExecutionEntity(entity);
        auditNoticeVo.setBizKey(String.valueOf(entity.getVariable(FlowConstant.FLOW_BIZKEY_VAR)));
        auditNoticeVo.setStarterId(String.valueOf(entity.getVariable(FlowConstant.FLOW_INITIATOR_VAR)));
    }

    @Override
    protected void processCancelled(FlowableCancelledEvent event) {
        log.info("流程取消. procInsId: {} ", event.getProcessInstanceId());
    }
}
