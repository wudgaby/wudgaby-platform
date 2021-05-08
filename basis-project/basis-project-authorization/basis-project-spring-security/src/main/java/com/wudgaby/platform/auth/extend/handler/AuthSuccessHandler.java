package com.wudgaby.platform.auth.extend.handler;

import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.utils.FastJsonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : AuthSuccessHandler
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 14:16
 * @Desc :   implements AuthenticationSuccessHandler
 */
@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        /*if(!HttpUtils.isAjaxRequest(request)){
            super.onAuthenticationSuccess(request, response, authentication);
        }*/

        ((UserInfo)authentication.getPrincipal()).setPassword(null);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = FastJsonUtil.collectToString(ApiResult.success().message("认证成功").data(authentication.getPrincipal()));
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
