package com.wudgaby.platform.auth.extend.dynamic.access;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

public class RoleAccessChecker implements AccessChecker{
    // 系统集合的抽象实现，这里你可以采用更加合理更加效率的方式
    private Supplier<Set<AntPathRequestMatcher>> supplier;

    @Override
    public boolean check(Authentication authentication, HttpServletRequest request) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 当前用户的角色集合
        System.out.println("authorities = " + authorities);
        //todo 这里自行实现比对逻辑
        // supplier.get().stream().filter(matcher -> matcher.matches(request));
        return false;
    }

    @Override
    public boolean check(Authentication authentication, String param) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(supplier.get(), "function must not be null");
    }
}
