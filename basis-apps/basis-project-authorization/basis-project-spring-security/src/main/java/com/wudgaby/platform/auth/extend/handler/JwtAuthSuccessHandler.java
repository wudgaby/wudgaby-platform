package com.wudgaby.platform.auth.extend.handler;

import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.utils.JacksonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/3 14:16
 * @Desc :   implements AuthenticationSuccessHandler
 */
//@Component
public class JwtAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        ((UserInfo)authentication.getPrincipal()).setPassword(null);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = JacksonUtil.serialize(ApiResult.success().message("认证成功").data(authentication.getPrincipal()));
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
