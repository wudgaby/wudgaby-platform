package com.wudgaby.platform.flowable.helper.vo;

import lombok.Data;

@Data
public class ProcessDefinitionStVo {
    private String category;
    private String categoryName;
    private String processDefinitionKey;
    private String processDefinitionName;
    private Long count;
}
