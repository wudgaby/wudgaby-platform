package com.wudgaby.platform.flowable.helper.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wudgaby.platform.flowable.helper.enums.DataStateEnum;
import com.wudgaby.platform.utils.LocalDateTimeUtil;
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
 * 角色表
 * </p>
 *
 * @author zouyong
 * @since 2020-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RoleTbl对象", description="角色表")
@TableName("oa_role")
public class RoleTbl implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "角色ID")
    @TableId(value = "role_id", type = IdType.UUID)
    private String roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色Code")
    private String roleCode;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "添加人用户ID")
    private String addUser;

    @ApiModelProperty(value = "系统预定")
    private Boolean withSystem;

    @ApiModelProperty(value = "0：正常  1：屏蔽")
    private DataStateEnum roleState;

    @ApiModelProperty(value = "是否展示")
    @TableField(value = "is_show")
    private Boolean isShow;

    @ApiModelProperty(value = "添加时间")
    @DateTimeFormat(pattern = LocalDateTimeUtil.COMMON_DATETIME)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = LocalDateTimeUtil.COMMON_DATETIME)
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
