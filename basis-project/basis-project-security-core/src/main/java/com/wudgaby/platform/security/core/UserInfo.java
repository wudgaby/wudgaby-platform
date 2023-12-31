package com.wudgaby.platform.security.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wudgaby.platform.springext.MetaResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/23 10:01
 * @Desc :
 */

@Data
@Accessors(chain = true)
@ApiModel("用户信息")
@NoArgsConstructor
public class UserInfo implements Serializable {

    @ApiModelProperty(value = "主键id")
    private Serializable id;

    @ApiModelProperty(value = "账号")
    private String account;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "状态 0:停用, 1:正常 ")
    private Integer status;

    @ApiModelProperty("权限信息")
    private Collection<String> authorities;

    @ApiModelProperty("角色编码")
    private Collection<String> roleCodes;

    @ApiModelProperty("请求资源")
    private Set<MetaResource> metaResources;

    @ApiModelProperty("角色详情")
    private Collection<RoleVo> roleList;

    @ApiModelProperty("管理员")
    private Boolean admin;

    @ApiModelProperty("租户")
    private Long tenantId;

    @ApiModelProperty("部门")
    private Long deptId;

    @ApiModelProperty("用户类型")
    private String userType;
}
