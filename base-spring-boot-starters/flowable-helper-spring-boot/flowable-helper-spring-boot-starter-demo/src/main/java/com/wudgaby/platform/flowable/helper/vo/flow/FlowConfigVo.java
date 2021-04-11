package com.wudgaby.platform.flowable.helper.vo.flow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName : FlowConfigVo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/7 11:37
 * @Desc :   流程配置
 */
@Data
@ApiModel("流程动态配置表单")
public class FlowConfigVo {
    /**
     * 流程名称
     */
    @ApiModelProperty(required = true)
    private String name;

    /**
     * 流程分组
     */
    @ApiModelProperty(required = true)
    private String group;

    /**
     * 流程pk
     */
    @ApiModelProperty(required = true)
    private String defKey;

    /**
     * 流程描述
     */
    private String desc;

    /**
     * 审批人去重
     */
    private Boolean autoRepeat = false;

    /**
     * 发起人审批自动通过
     */
    private Boolean autoApprove = true;

    /**
     * 审批意见提示信息
     */
    private String commentHint;

    /**
     * 节点列表
     */
    private List<NodeConfigVo> nodeConfigList;

    /**
     * 优化
     */
    private Boolean optimized  = true;

    /**
     * 退回至发起人
     */
    private Boolean rejectToInitiator = false;

    /**
     * 没人审批跳过
     */
    private Boolean nobodySkip = true;

    /**
     * 默认审批人
     */
    private String defaultApprover;

}
