package com.wudgaby.platform.sso.server.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/12/8 10:17
 * @Desc :
 */
@Data
public class ChangePwdForm {
    @ApiModelProperty("旧密码")
    @NotBlank(message = "请输入旧密码")
    private String oldPwd;

    @ApiModelProperty("新密码")
    @NotBlank(message = "请输入新密码")
    private String newPwd;
}
