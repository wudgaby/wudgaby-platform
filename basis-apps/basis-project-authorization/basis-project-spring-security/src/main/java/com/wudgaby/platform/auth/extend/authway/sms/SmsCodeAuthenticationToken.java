package com.wudgaby.platform.auth.extend.authway.sms;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @ClassName : SmsCodeAuthenticationToken
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 19:41
 * @Desc :
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken{
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;
    private String captcha;

    public SmsCodeAuthenticationToken(Object mobile, String captcha){
        super(null);
        this.principal = mobile;
        this.captcha = captcha;
        setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(Object mobile, String captcha,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = mobile;
        this.captcha = captcha;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return captcha;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
