package com.wudgaby.platform.sso.core.interceptor;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoRemoteHelper;
import com.wudgaby.platform.sso.core.helper.SsoTokenHelper;
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
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : SsoWebInterceptor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:21
 * @Desc :
 */
@Slf4j
@RequiredArgsConstructor
public class SsoTokenInterceptor implements HandlerInterceptor {
    private final SsoTokenHelper ssoTokenHelper;
    private final SsoProperties ssoProperties;
    private final SsoRemoteHelper ssoRemoteHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        if (StringUtils.equals(ssoProperties.getLogoutPath(), servletPath)) {
            ssoTokenHelper.logout(request);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            ApiResult apiResult = ApiResult.success().message("登出成功");
            response.getWriter().println(FastJsonUtil.collectToString(apiResult));
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

        //本系统直接从redis校验有效性
        //SsoUserVo ssoUserVo = ssoTokenHelper.loginCheck(request);

        //直接从子系统会话中判断已登录
        /*SsoUserVo sessionSsoUserVo = (SsoUserVo)request.getSession().getAttribute(SsoConst.SSO_USER);
        if(sessionSsoUserVo != null){
            //为了 刷新redis中的有效期
            ssoRemoteHelper.remoteCheck(sessionSsoUserVo.getToken());
            return true;
        }*/

        //去sso服务器校验有效性
        String headerSessionId = request.getHeader(SsoConst.SSO_HEADER_X_TOKEN);
        SsoUserVo ssoUserVo = ssoRemoteHelper.remoteCheck(headerSessionId);

        if (ssoUserVo == null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ApiResult apiResult = ApiResult.failure().message("未登录");
            response.getWriter().println(FastJsonUtil.collectToString(apiResult));
            return false;
        }

        //放入本系统会话中.
        if(request.getSession().getAttribute(SsoConst.SSO_USER) == null){
            request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
        }

        SsoSecurityUtils.setUserInfo(ssoUserVo);
        return true;
    }
}
