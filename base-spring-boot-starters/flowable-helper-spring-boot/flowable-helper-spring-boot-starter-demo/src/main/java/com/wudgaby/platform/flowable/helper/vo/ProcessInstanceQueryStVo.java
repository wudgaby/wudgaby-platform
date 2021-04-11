package com.wudgaby.platform.flowable.helper.vo;

import lombok.Data;

@Data
public class ProcessInstanceQueryStVo{
    private String processDefinitionCategory;
    private String processInstanceId;
    private String processInstanceName;
    private String processDefinitionName;
    private String processDefinitionKey;
    private String processDefinitionId;
    private String businessKey;
    private String involvedUser;
    private Boolean finished = false;
    private Boolean unfinished = false;
    private String superProcessInstanceId;
    private Boolean excludeSubprocesses = false;
    private String finishedAfter;
    private String finishedBefore;
    private String startedAfter;
    private String startedBefore;
    private String startedBy;
    private Boolean startedByMe = false;
    private Boolean ccToMe = false;

    private Integer current = 1;
    private Integer size = 10;
    private String tenantId;
    private String orderRule;
    private String userId;
}
