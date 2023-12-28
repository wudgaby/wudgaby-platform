package com.wudgaby.oauth2.uaa.enhance.mobile;

import com.wudgaby.oauth2.uaa.enhance.base.MyAbstractUserDetailsAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/10/8 20:04
 * @Desc : 手机号登录校验逻辑
 */
@Slf4j
public class MobileAuthenticationProvider extends MyAbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            log.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();

        //获取短信验证码key为手机号
        String smsCode = "10000";
        if (!smsCode.equals(presentedPassword)) {
            log.debug("Authentication failed: verifyCode does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, Authentication authentication) throws AuthenticationException {
        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }catch (UsernameNotFoundException ex) {
            throw ex;
        }catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    /**
     * 从数据库获取或通过feign
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    /*@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        String mobile = (String) mobileAuthenticationToken.getPrincipal();

        UserDetails loadedUser = userDetailsService.loadUserByUsername(mobile);
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("手机号不存在:" + mobileAuthenticationToken.getPrincipal());
        }
        MobileAuthenticationToken tokenResult = new MobileAuthenticationToken(mobileAuthenticationToken.getPrincipal(),loadedUser.getAuthorities());
        tokenResult.setDetails(tokenResult.getDetails());
        return tokenResult;
    }*/

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        MobileAuthenticationToken result = new MobileAuthenticationToken(principal, authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
