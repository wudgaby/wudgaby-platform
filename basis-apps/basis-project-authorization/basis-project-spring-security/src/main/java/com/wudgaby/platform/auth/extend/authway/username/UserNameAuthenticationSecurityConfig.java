package com.wudgaby.platform.auth.extend.authway.username;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName : UserNameAuthenticationSecurityConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 21:28
 * @Desc :
 */
@Component
public class UserNameAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationSuccessHandler authSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authFailureHandler;

    @Resource(name = "accountUserService")
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AjaxUsernamePasswordAuthenticationFilter ajaxUsernamePasswordAuthenticationFilter = new AjaxUsernamePasswordAuthenticationFilter();
        ajaxUsernamePasswordAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        ajaxUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        ajaxUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(authFailureHandler);

        UserNameAuthenticationProvider authenticationProvider = new UserNameAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(ajaxUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
