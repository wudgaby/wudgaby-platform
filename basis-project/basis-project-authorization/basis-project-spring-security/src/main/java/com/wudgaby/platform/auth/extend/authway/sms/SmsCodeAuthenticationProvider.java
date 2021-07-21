package com.wudgaby.platform.auth.extend.authway.sms;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        UserDetails loadUser = userDetailsService.loadUserByUsername((String)authenticationToken.getPrincipal());

        if(loadUser == null){
            throw new InternalAuthenticationServiceException("获取用户信息失败.");
        }

        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(loadUser, loadUser.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
