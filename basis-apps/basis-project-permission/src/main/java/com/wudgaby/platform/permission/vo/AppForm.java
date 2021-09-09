package com.wudgaby.platform.permission.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/15 15:14
 * @Desc :
 */
@ApiModel("应用表单")
@Data
public class AppForm {
    @ApiModelProperty(value = "客户端ID", hidden = true)
    private Long appId;

    @ApiModelProperty(value = "app名称", required = true)
    @Length(min = 2, max = 20, message = "请填写app名称(2-20).")
    private String appName;

    @ApiModelProperty(value = "app英文名称")
    @Length(min = 2, max = 20, message = "请填写app英文名称(2-50).")
    private String appNameEn;

    @ApiModelProperty(value = "app图标")
    private String appIcon;

    @ApiModelProperty(value = "app类型: server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用")
    @NotBlank(message = "请选择app类型")
    private String appType;

    @ApiModelProperty(value = "app描述")
    private String appDesc;

    @ApiModelProperty(value = "移动应用操作系统:ios-苹果 android-安卓")
    private String appOs;

    @ApiModelProperty(value = "官网地址")
    @NotBlank(message = "请填写官网地址")
    private String website;

    @ApiModelProperty(value = "开发者ID:默认为0")
    private Long developerId;
}
