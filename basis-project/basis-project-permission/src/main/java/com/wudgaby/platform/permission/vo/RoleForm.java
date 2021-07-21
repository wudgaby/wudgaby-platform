package com.wudgaby.platform.permission.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/17 0:15
 * @Desc :
 */
@ApiModel("角色表单")
@Data
public class RoleForm {
    @ApiModelProperty(value = "角色ID", hidden = true)
    private Long roleId;

    @ApiModelProperty(value = "角色编码", required = true)
    @NotBlank(message = "请填写菜角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称", required = true)
    @NotBlank(message = "请填写角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;
}
