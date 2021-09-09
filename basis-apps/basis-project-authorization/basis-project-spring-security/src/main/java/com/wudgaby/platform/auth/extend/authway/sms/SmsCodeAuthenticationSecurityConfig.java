package com.wudgaby.platform.auth.extend.authway.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName : SmsCodeAuthenticationSecurityConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 21:28
 * @Desc :  1. 先经过 SmsAuthenticationFilter，构造一个没有鉴权的 SmsAuthenticationToken，然后交给 AuthenticationManager 处理。
 *          2. AuthenticationManager 通过 for-each 挑选出一个合适的 provider 进行处理，当然我们希望这个 provider 要是 SmsAuthenticationProvider。
 *          3. 验证通过后，重新构造一个有鉴权的 SmsAuthenticationToken，并返回给 SmsAuthenticationFilter。
 *          4. filter 根据上一步的验证结果，跳转到成功或者失败的处理逻辑。
 */
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private AuthenticationSuccessHandler authSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authFailureHandler;

    @Resource(name = "phoneUserService")
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsValidateCodeFilter smsValidateCodeFilter;

    @Autowired
    private CaptchaService captchaService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationProcessingFilter authenticationProcessingFilter = new SmsCodeAuthenticationProcessingFilter();
        authenticationProcessingFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationProcessingFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        authenticationProcessingFilter.setAuthenticationFailureHandler(authFailureHandler);

        SmsCodeAuthenticationProvider authenticationProvider = new SmsCodeAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        //使用Provider内部校验验证码方式.
        authenticationProvider.setCaptchaService(captchaService);
        http.authenticationProvider(authenticationProvider)
                .addFilterAfter(authenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                //使用增加filter链校验验证码方式
                .addFilterBefore(smsValidateCodeFilter, SmsCodeAuthenticationProcessingFilter.class);
    }
}
