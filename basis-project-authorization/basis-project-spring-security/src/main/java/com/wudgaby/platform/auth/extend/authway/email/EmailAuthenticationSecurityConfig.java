package com.wudgaby.platform.auth.extend.authway.email;

import com.wudgaby.platform.auth.extend.authway.ImageValidateCodeFilter;
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
 * @ClassName : EmailAuthenticationSecurityConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 21:28
 * @Desc :   TODO
 */
@Component
public class EmailAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private AuthenticationSuccessHandler authSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authFailureHandler;

    @Autowired
    private ImageValidateCodeFilter imageValidateCodeFilter;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "mailUserService")
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        EmailAuthenticationProcessingFilter authenticationProcessingFilter = new EmailAuthenticationProcessingFilter();
        authenticationProcessingFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationProcessingFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        authenticationProcessingFilter.setAuthenticationFailureHandler(authFailureHandler);

        EmailAuthenticationProvider authenticationProvider = new EmailAuthenticationProvider();
        //不行 因Only UsernamePasswordAuthenticationToken is supported
        //EmailUserDetailsAuthenticationProvider authenticationProvider = new EmailUserDetailsAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        http.authenticationProvider(authenticationProvider)
                .addFilterAfter(authenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageValidateCodeFilter, EmailAuthenticationProcessingFilter.class);
    }
}
