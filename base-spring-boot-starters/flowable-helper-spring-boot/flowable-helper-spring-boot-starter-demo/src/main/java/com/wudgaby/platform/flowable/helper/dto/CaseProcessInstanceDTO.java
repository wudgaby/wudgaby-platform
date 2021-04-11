package com.wudgaby.platform.flowable.helper.dto;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName : CaseFlowTasDTO
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/21 11:38
 * @Desc :   TODO
 */
@Data
@ApiModel("案情任务流程传输对象")
public class CaseProcessInstanceDTO {
    @ApiModelProperty("案件名称")
    private String caseName;

    @ApiModelProperty("发起人id")
    private Integer staterId;

    @ApiModelProperty("发起人名称")
    private String staterName;

    @ApiModelProperty("审核状态")
    private String auditState;

    @ApiModelProperty("流程定义id")
    private String processDefinitionId;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("关联的业务主键")
    private String businessKey;

    @ApiModelProperty("流程开始时间")
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    private Date startTime;
}
