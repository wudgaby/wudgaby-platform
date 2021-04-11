package com.wudgaby.platform.flowable.helper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author WudGaby
 * @since 2020-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FlowConfig对象", description="")
public class FlowConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "流程分组类")
    private String groupName;

    @ApiModelProperty(value = "流程定义key")
    private String defKey;

    @ApiModelProperty(value = "流程定义id")
    private String defId;

    @ApiModelProperty(value = "描述")
    @TableField(value = "`desc`")
    private String desc;

    @ApiModelProperty(value = "自动去重")
    private Boolean autoRepeat;

    @ApiModelProperty(value = "发起人自动审批通过")
    private Boolean autoApprove;

    @ApiModelProperty(value = "提示信息")
    private String commentHint;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "优化")
    @TableField(value = "is_optimized")
    private Boolean optimized;

    @ApiModelProperty(value = "驳回至发起人")
    @TableField(value = "is_reject_to_initiator")
    private Boolean rejectToInitiator;

    @ApiModelProperty(value = "没人审批跳过")
    @TableField(value = "is_nobody_skip")
    private Boolean nobodySkip = true;

    @ApiModelProperty(value = "没人审批时 指定审批人")
    @TableField(value = "default_approver")
    private String defaultApprover;

}
