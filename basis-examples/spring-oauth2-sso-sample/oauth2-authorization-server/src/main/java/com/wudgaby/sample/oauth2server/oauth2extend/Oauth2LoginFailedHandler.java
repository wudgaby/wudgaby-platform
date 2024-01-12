package com.wudgaby.sample.oauth2server.oauth2extend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/11 0011 19:27
 * @desc :
 */
@Slf4j
@Component
public class Oauth2LoginFailedHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error(exception.getMessage(), exception);
        super.onAuthenticationFailure(request, response, exception);
    }
}
