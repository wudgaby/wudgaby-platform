package com.wudgaby.oauth2.uaa.enhance.mobile;

import com.wudgaby.oauth2.uaa.enhance.base.MyAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/10/8 20:01
 * @Desc :   手机号登录令牌
 */
public class MobileAuthenticationToken extends MyAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public MobileAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MobileAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
