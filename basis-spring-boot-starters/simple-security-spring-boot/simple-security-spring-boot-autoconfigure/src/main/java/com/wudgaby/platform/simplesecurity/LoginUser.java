package com.wudgaby.platform.simplesecurity;

import com.wudgaby.platform.simplesecurity.ext.MetaResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * @ClassName : LoginUser
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/23 10:01
 * @Desc :
 */

@Data
@Accessors(chain = true)
@ApiModel("登录用户信息")
@NoArgsConstructor
public class LoginUser {

    @ApiModelProperty(value = "主键id")
    private Serializable id;

    @ApiModelProperty(value = "账号")
    private String account;

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

    @ApiModelProperty("权限")
    private Collection<String> authorities;

    @ApiModelProperty("角色")
    private Collection<String> roles;

    @ApiModelProperty("请求资源")
    private Set<MetaResource> metaResources;

    @ApiModelProperty("是否管理员")
    private Boolean admin;

    public LoginUser verifyAdmin(String[] adminRoleCodes) {
        if(ArrayUtils.isEmpty(adminRoleCodes) || CollectionUtils.isEmpty(roles)){
            admin = false;
            return this;
        }

        for (String authority : roles) {
            for(String roleCode : adminRoleCodes){
                if (authority.equals(roleCode)) {
                    admin = true;
                    return this;
                }
            }
        }

        admin = false;
        return this;
    }
}
