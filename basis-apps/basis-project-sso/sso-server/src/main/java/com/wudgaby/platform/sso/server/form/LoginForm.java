package com.wudgaby.platform.sso.server.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName : LoginForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/8 10:17
 * @Desc :
 */
@Data
public class LoginForm {
    @ApiModelProperty("账号")
    @NotBlank(message = "请输入账号")
    private String account;

    @ApiModelProperty("密码")
    @NotBlank(message = "请输入密码")
    private String password;

    @ApiModelProperty("应用标识")
    @NotBlank(message = "缺少应用标识")
    private String appCode;
}
