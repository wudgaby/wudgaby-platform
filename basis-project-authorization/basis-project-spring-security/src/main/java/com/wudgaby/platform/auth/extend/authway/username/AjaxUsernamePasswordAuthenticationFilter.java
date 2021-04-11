package com.wudgaby.platform.auth.extend.authway.username;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName : AjaxUsernamePasswordAuthenticationFilter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/14 14:48
 * @Desc :   TODO
 */
public class AjaxUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public AjaxUsernamePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/ajaxLogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //attempt Authentication when Content-Type is json
        if(request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)){
            //use jackson to deserialize json
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()){
                LoginForm loginForm = mapper.readValue(is, LoginForm.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        loginForm.getUsername(), loginForm.getPassword());
            }catch (IOException e) {
                authRequest = new UsernamePasswordAuthenticationToken("", "");
            }finally {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }

        throw new AuthenticationServiceException(
                "Authentication method not supported: " + request.getMethod());
    }

    protected void setDetails(HttpServletRequest request,
                              AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}