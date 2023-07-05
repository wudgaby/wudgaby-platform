package com.wudgaby.platform.auth.extend.strategy;

import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.JacksonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : MyInvalidSessionStrategy
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/5 20:56
 * @Desc :
 */
public class MyInvalidSessionStrategy implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //清除cookie
        Cookie session = new Cookie("SESSION", "");
        session.setMaxAge(0);
        session.setPath("/");
        response.addCookie(session);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = JacksonUtil.serialize(ApiResult.<String>failure().message("无效会话,请重新登录."));
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
