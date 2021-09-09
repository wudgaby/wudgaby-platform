package com.wudgaby.platform.sso.server.form;

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
    @NotBlank(message = "请输入账号")
    private String account;

    @NotBlank(message = "请输入密码")
    private String password;
}
