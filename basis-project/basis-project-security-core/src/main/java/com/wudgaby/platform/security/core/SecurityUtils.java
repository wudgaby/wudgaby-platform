package com.wudgaby.platform.security.core;

/**
 * @ClassName : SecurityUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/29 9:48
 * @Desc :   
 */

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * @ClassName : SecurityUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/7 17:31
 * @Desc :   
 */
@Slf4j
@UtilityClass
public class SecurityUtils {
    public Optional<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("no authentication in security context found");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }
        log.debug("found username '{}' in security context", username);
        return Optional.ofNullable(username);
    }

    public Optional<UserInfo> getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("no authentication in security context found");
            return Optional.empty();
        }

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserInfo user = (UserInfo) authentication.getPrincipal();
            return Optional.ofNullable(user);
        } else if (authentication.getPrincipal() instanceof String) {
            UserInfo user = new UserInfo().setAccount((String) authentication.getPrincipal());
            return Optional.ofNullable(user);
        }
        return Optional.empty();
    }

    public UserInfo getSafeCurrentUser() {
        return getCurrentUser().orElseGet(() -> new UserInfo().setId(0L).setUsername("anonymous"));
    }
}
