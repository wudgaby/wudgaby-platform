package com.wudgaby.platform.sso.server.entity;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
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
 * 系统应用-基础信息
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BaseApp对象", description="系统应用-基础信息")
public class BaseApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端ID")
    @TableId(value = "app_id", type = IdType.ASSIGN_ID)
    private Long appId;

    @ApiModelProperty(value = "APP标识")
    private String appCode;

    @ApiModelProperty(value = "API访问key")
    private String appKey;

    @ApiModelProperty(value = "API访问密钥")
    private String appSecret;

    @ApiModelProperty(value = "app名称")
    private String appName;

    @ApiModelProperty(value = "app英文名称")
    private String appNameEn;

    @ApiModelProperty(value = "应用图标")
    private String appIcon;

    @ApiModelProperty(value = "app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用")
    private String appType;

    @ApiModelProperty(value = "app描述")
    private String appDesc;

    @ApiModelProperty(value = "移动应用操作系统:ios-苹果 android-安卓")
    private String appOs;

    @ApiModelProperty(value = "官网地址")
    private String website;

    @ApiModelProperty(value = "开发者ID:默认为0")
    private Long developerId;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;

    @ApiModelProperty(value = "保留数据0-否 1-是 不允许删除")
    private Integer isPersist;


}
