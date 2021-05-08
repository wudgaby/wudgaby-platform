package com.wudgaby.platform.flowable.helper.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wudgaby.platform.flowable.helper.enums.DataStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zouyong
 * @since 2020-02-17
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "UserTbl对象", description = "用户表")
@TableName("oa_user")
public class UserTbl{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "user_id", type = IdType.UUID)
    private String userId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "用户密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "名称")
    @TableField("username")
    private String name;

    @ApiModelProperty(value = "警号")
    private String policeNo;

    @ApiModelProperty(value = "用户部门")
    private Integer userDept;

    @ApiModelProperty(value = "用户手机")
    private String userTel;

    @ApiModelProperty(value = "用户添加id")
    private String addUserId;

    @ApiModelProperty(value = "用户Email")
    private String userEmail;

    @ApiModelProperty(value = "0：男 1：女")
    private Integer userSexy;

    @ApiModelProperty(value = "用户年龄")
    private Integer userAge;

    @ApiModelProperty(value = "用户头像URL")
    private String userPortrait;

    @ApiModelProperty(value = "用户地址")
    private String userAddr;

    @ApiModelProperty(value = "用户所在省份")
    private String userProvince;

    @ApiModelProperty(value = "用户所在城市")
    private String userCity;

    @ApiModelProperty(value = "用户所在地区")
    private String userArea;

    @ApiModelProperty(value = "用户所在街道")
    private String userStreet;

    @ApiModelProperty(value = "用户居住地址房间号")
    private String userRoom;

    @ApiModelProperty(value = "用户可用状态 0：正常  1 屏蔽")
    private DataStateEnum userStatus;

    @ApiModelProperty(value = "是否删除")
    @TableField(value = "is_deleted")
    private Boolean deleted;

    @ApiModelProperty(value = "用户添加时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

}
