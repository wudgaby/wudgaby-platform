package com.wudgaby.platform.auth.extend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/19 23:04
 * @Desc :
 */
@Slf4j
@Configuration
public class CustomPermissionEvaluator implements PermissionEvaluator {
    //普通的targetDomainObject判断
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String privilege = String.valueOf(permission);
        for(GrantedAuthority authority : authentication.getAuthorities()){
            if(privilege.equalsIgnoreCase(authority.getAuthority())){
                return true;
            }
        }
        return false;
    }

    //用于ACL的访问控制
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new RuntimeException("Id-based permission evaluation not currently supported.");
    }
}
