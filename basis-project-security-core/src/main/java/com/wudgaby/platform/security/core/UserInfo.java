package com.wudgaby.platform.security.core;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @ClassName : UserInfo
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/23 10:01
 * @Desc :   TODO
 */

@Data
@Accessors(chain = true)
@ApiModel("用户信息")
@NoArgsConstructor
public class UserInfo implements UserDetails {

    @ApiModelProperty(value = "主键id")
    private Object id;

    @ApiModelProperty(value = "账号")
    private String account;

    @JSONField(serialize = false)
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
    private Collection<? extends GrantedAuthority> authorities;

    @ApiModelProperty("管理员")
    private Boolean admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return account;
    }

    public String getName() {
        return username;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return status == 1;
    }

    public UserInfo verifyAdmin(String... adminRoleCodes) {
        if(adminRoleCodes == null || adminRoleCodes.length == 0){
            admin = false;
            return this;
        }


        for (GrantedAuthority grantedAuthority : getAuthorities()) {
            for(String roleCode : adminRoleCodes){
                if (grantedAuthority.getAuthority().equals(roleCode)) {
                    admin = true;
                    return this;
                }
            }
        }

        admin = false;
        return this;
    }
}
