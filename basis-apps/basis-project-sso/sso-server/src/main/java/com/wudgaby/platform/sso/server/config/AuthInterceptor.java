package com.wudgaby.platform.sso.server.config;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.annotations.AnonymousAccess;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.utils.SsoSecurityUtils;
import com.wudgaby.platform.sso.core.utils.WebUtil;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : AuthInterceptor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:21
 * @Desc :
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        //匿名访问 放行
        HandlerMethod hm = (HandlerMethod)handler;
        AnonymousAccess anonymousAccess = AnnotationUtils.getAnnotation(hm.getMethod(), AnonymousAccess.class);
        if(anonymousAccess != null){
            return true;
        }

        //校验token是否一致
        SsoUserVo ssoUserVo = (SsoUserVo)request.getSession().getAttribute(SsoConst.SSO_USER);
        if(ssoUserVo == null){
            if (WebUtil.isAjax(request) || WebUtil.isContentTypeJson(request)) {
                ApiResult apiResult = ApiResult.failure().message("未登录").data(SsoConst.SSO_LOGIN_URL).code(401);
                output(response, FastJsonUtil.collectToString(apiResult));
            } else {
                response.sendRedirect(SsoConst.SSO_LOGIN_URL);
            }
            return false;
        }

        //放入本系统会话中.
        request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
        //保存用户信息到线程中
        SsoSecurityUtils.setUserInfo(ssoUserVo);
        return true;
    }

    private void output(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().println(message);
    }
}
