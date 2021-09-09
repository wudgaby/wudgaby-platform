package com.wudgaby.platform.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName : UpdatePwdForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2021/1/2 17:50
 * @Desc :   
 */
@Data
@ApiModel("更新密码表单")
public class UpdatePwdForm {
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "请确认修改的用户id")
    private Long id;

    @ApiModelProperty(value = "旧密码", required = true)
    @NotBlank(message = "请输入旧密码")
    private String oldPwd;

    @ApiModelProperty(value = "新密码", required = true)
    @NotNull(message = "请输入新密码")
    private String newPwd;
}
