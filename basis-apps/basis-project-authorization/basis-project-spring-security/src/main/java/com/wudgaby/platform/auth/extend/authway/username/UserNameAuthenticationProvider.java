package com.wudgaby.platform.auth.extend.authway.username;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName : UserNameAuthenticationProvider
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 19:45
 * @Desc :   
 */
@Slf4j
@Data
public class UserNameAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        UserDetails loadUser = userDetailsService.loadUserByUsername((String)authenticationToken.getPrincipal());

        if(loadUser == null){
            throw new InternalAuthenticationServiceException("获取用户信息失败.");
        }

        checkPassword(authentication, loadUser);

        UsernamePasswordAuthenticationToken authenticationResult = new UsernamePasswordAuthenticationToken(loadUser, authenticationToken.getCredentials(), loadUser.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    private void checkPassword(Authentication authentication, UserDetails userDetails){
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("账号或密码错误");
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("账号或密码错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
