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
 * @Date :  2021/7/17 0:14
 * @Desc :
 */
@ApiModel("用户表单")
@Data
public class UserForm {
    @ApiModelProperty(value = "用户ID", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "登陆账号", required = true)
    @Length(min = 5, max = 20, message = "请填写角色名称(5-20)")
    private String userName;

    @ApiModelProperty(value = "昵称", required = true)
    @Length(min = 2, max = 20, message = "请填写角色名称(2-20)")
    private String nickName;

    @ApiModelProperty(value = "密码", required = true)
    @Length(min = 6, max = 50, message = "请填写密码(6-50)")
    private String password;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户类型:super-超级管理员 normal-普通管理员")
    private String userType;

    @ApiModelProperty(value = "企业ID")
    private Long companyId;

    @ApiModelProperty(value = "描述")
    private String userDesc;
}
