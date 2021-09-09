package com.wudgaby.oauth2.uaa.enhance.qrcode;

import com.wudgaby.oauth2.uaa.enhance.mobile.MobileAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @ClassName : QRCodeAuthenticationProvider
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/10/8 20:04
 * @Desc : 登录校验逻辑
 */
@Slf4j
public class QRCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * 从数据库获取或通过feign
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        QRCodeAuthenticationToken qrCodeAuthenticationToken = (QRCodeAuthenticationToken) authentication;
        String code = (String) qrCodeAuthenticationToken.getPrincipal();

        UserDetails loadedUser = userDetailsService.loadUserByUsername(code);
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("未登录:" + qrCodeAuthenticationToken.getPrincipal());
        }
        MobileAuthenticationToken tokenResult = new MobileAuthenticationToken(qrCodeAuthenticationToken.getPrincipal(),loadedUser.getAuthorities());
        tokenResult.setDetails(tokenResult.getDetails());
        return tokenResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return QRCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
