package com.wudgaby.platform.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : SecurityBeanConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/5 20:02
 * @Desc :   
 */
@Configuration
public class SecurityBeanConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        //密码认证保持兼容
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setValiditySeconds((int) TimeUnit.DAYS.toSeconds(1));
        return rememberMeServices;
    }

    /**
     *  https://www.cnblogs.com/crazymakercircle/p/12037747.html
     *  sessionid存放位置
     */
    /*@Bean
    public HeaderHttpSessionIdResolver headerHttpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }*/
}
