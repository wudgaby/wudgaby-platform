package com.wudgaby.platform.flowable.helper.vo;

import lombok.Data;
import org.flowable.common.engine.api.delegate.event.FlowableEventType;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;

import java.util.Set;

/**
 * @ClassName : AuditNoticeVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/26 9:29
 * @Desc :
 */
@Data
public class AuditNoticeVo {
    private FlowableEventType eventType;
    private String title;
    private String message;
    private String procInstId;
    private String taskId;
    private String auditType;
    private String assignee;
    private Set<IdentityLink> candidates;
    private String bizKey;
    private Task task;
    private ExecutionEntity executionEntity;
    private String starterId;
}
