package com.wudgaby.platform.auth.extend.handler;

import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : AuthFailureHandler
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 14:20
 * @Desc :   TODO
 */
@Slf4j
@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        /*if(!HttpUtils.isAjaxRequest(request)){
            super.onAuthenticationFailure(request, response, exception);
            return;
        }*/
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = FastJsonUtil.collectToString(ApiResult.<String>failure().message(exception.getMessage()));
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
