package com.wudgaby.platform.auth.extend.authway.sms;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @ClassName : SmsCodeAuthenticationProvider
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 19:45
 * @Desc :   
 */
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;
    private CaptchaService captchaService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken) authentication;

        // 验证码校验
        if (!captchaService.verifyCaptchaCode((String)smsCodeAuthenticationToken.getPrincipal(), (String)smsCodeAuthenticationToken.getCredentials())) {
            throw new BadCredentialsException("验证码不匹配");
        }

        UserDetails loadUser = userDetailsService.loadUserByUsername((String)smsCodeAuthenticationToken.getPrincipal());
        if(loadUser == null){
            throw new InternalAuthenticationServiceException("获取用户信息失败.");
        }

        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(loadUser, (String)smsCodeAuthenticationToken.getCredentials(), loadUser.getAuthorities());
        authenticationResult.setDetails(smsCodeAuthenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
