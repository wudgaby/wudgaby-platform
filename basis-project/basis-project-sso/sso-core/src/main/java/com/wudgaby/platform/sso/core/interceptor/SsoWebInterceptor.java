package com.wudgaby.platform.sso.core.interceptor;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoRemoteHelper;
import com.wudgaby.platform.sso.core.helper.SsoWebHelper;
import com.wudgaby.platform.sso.core.permission.AnonymousAccess;
import com.wudgaby.platform.sso.core.permission.SsoSecurityUtils;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName : SsoWebInterceptor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:21
 * @Desc :
 */
@Slf4j
@RequiredArgsConstructor
public class SsoWebInterceptor implements HandlerInterceptor {
    private final SsoWebHelper ssoWebHelper;
    private final SsoProperties ssoProperties;
    private final SsoRemoteHelper ssoRemoteHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        if (StringUtils.equals(ssoProperties.getLogoutPath(), servletPath)) {
            ssoWebHelper.removeSessionIdByCookie(request, response);

            String logoutPageUrl = ssoProperties.getServer().concat(SsoConst.SSO_LOGOUT_URL);
            response.sendRedirect(logoutPageUrl);
            return false;
        }

        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        HandlerMethod hm = (HandlerMethod)handler;
        AnonymousAccess anonymousAccess = AnnotationUtils.getAnnotation(hm.getMethod(), AnonymousAccess.class);
        //匿名访问 放行
        if(anonymousAccess != null){
            return true;
        }

        SsoUserVo ssoUserVo = ssoWebHelper.loginCheck(request, response, false);

        if (ssoUserVo == null) {
            String header = request.getContentType();
            boolean isJson =  StringUtils.contains(header, "json");
            if (isJson) {
                //json提示
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                ApiResult apiResult = ApiResult.failure().message("未登录");
                response.getWriter().println(FastJsonUtil.collectToString(apiResult));
                return false;
            } else {
                //网页跳转
                String link = request.getRequestURL().toString();
                String loginPageUrl = ssoProperties.getServer().concat(SsoConst.SSO_LOGIN_URL) + "?" + SsoConst.REDIRECT_URL + "=" + link;
                response.sendRedirect(loginPageUrl);
                return false;
            }
        }

        //放入本系统会话中.
        if(request.getSession().getAttribute(SsoConst.SSO_USER) == null){
            request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
        }

        //用户信息
        SsoSecurityUtils.setUserInfo(ssoUserVo);
        return true;
    }


}
