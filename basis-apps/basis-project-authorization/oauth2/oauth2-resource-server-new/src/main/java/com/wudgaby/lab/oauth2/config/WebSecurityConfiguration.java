package com.wudgaby.lab.oauth2.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/10/4 13:33
 * @Desc :
 */
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authz -> authz.anyRequest().authenticated());
    }
}
