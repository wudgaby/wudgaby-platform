package com.wudgaby.platform.permission.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统权限-菜单权限、操作权限、API权限
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BaseAuthority对象", description="系统权限-菜单权限、操作权限、API权限")
public class BaseAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "authority_id", type = IdType.ASSIGN_ID)
    private Long authorityId;

    @ApiModelProperty(value = "权限标识")
    private String authority;

    @ApiModelProperty(value = "菜单资源ID")
    private Long menuId;

    @ApiModelProperty(value = "API资源ID")
    private Long apiId;

    @ApiModelProperty(value = "功能资源ID")
    private Long actionId;

    @ApiModelProperty(value = "状态")
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


}
