package com.wudgaby.sample.oauth2server.oauth2extend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/11 0011 19:26
 * @desc :
 */
@Slf4j
@Component
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            String authorizedProvider = oauth2Token.getAuthorizedClientRegistrationId();
            log.info("authorizedProvider为{}", authorizedProvider);

            if (authorizedProvider.equals("github")) {
                DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
                //查询数据库,是否存在. 不存在注册, 存在直接返回token.
                //response.sendRedirect("/signup");
            } else if (authorizedProvider.equals("gitee")) {
                GiteeOAuth2User giteeOAuth2User = (GiteeOAuth2User) authentication.getPrincipal();
                //查询数据库,是否存在. 不存在注册, 存在直接返回token.
                //response.sendRedirect("/signup");
            } else {
                log.info("不支持的oauth2 provider");
                //response.sendRedirect("/signup");
            }
        }
    }
}
