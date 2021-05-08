package com.wudgaby.platform.auth.extend.entrypoint;

import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.FastJsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : JsonAuthenticationEntryPoint
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/5 0:19
 * @Desc :   TODO
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = FastJsonUtil.collectToString(ApiResult.<String>failure().message("未认证"));
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
