package com.wudgaby.platform.permission.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 系统资源-功能操作
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BaseAction对象", description="系统资源-功能操作")
public class BaseAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源ID")
    @TableId(value = "action_id", type = IdType.ASSIGN_ID)
    private Long actionId;

    @ApiModelProperty(value = "资源编码")
    private String actionCode;

    @ApiModelProperty(value = "资源名称")
    private String actionName;

    @ApiModelProperty(value = "资源描述")
    private String actionDesc;

    @ApiModelProperty(value = "资源父节点")
    private Long menuId;

    @ApiModelProperty(value = "优先级 越小越靠前")
    private Integer priority;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "保留数据0-否 1-是 不允许删除")
    private Integer isPersist;

    @ApiModelProperty(value = "服务名称")
    private String serviceId;


}
