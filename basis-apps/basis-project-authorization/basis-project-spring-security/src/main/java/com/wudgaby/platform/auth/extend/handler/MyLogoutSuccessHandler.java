package com.wudgaby.platform.auth.extend.handler;

import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.JacksonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : MyLogoutSuccessHandler
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 16:48
 * @Desc :   
 */
@Component
public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*if(!HttpUtils.isAjaxRequest(request)){
            super.onLogoutSuccess(request, response, authentication);
            return;
        }*/

        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String json = JacksonUtil.serialize(ApiResult.success().message("登出成功"));
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
