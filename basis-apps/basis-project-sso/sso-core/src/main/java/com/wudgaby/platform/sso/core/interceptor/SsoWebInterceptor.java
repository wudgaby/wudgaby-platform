package com.wudgaby.platform.sso.core.interceptor;

import cn.hutool.core.net.url.UrlBuilder;
import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.annotations.AnonymousAccess;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoRemoteHelper;
import com.wudgaby.platform.sso.core.utils.SsoSecurityUtils;
import com.wudgaby.platform.sso.core.utils.WebUtil;
import com.wudgaby.platform.sso.core.vo.SsoTokenVo;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.wudgaby.platform.sso.core.constant.SsoConst.PART_SESSION_TOKEN_ATTR;

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
    private final SsoProperties ssoProperties;
    private final SsoRemoteHelper ssoRemoteHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        //登出
        String servletPath = request.getServletPath();
        if (StringUtils.equals(ssoProperties.getLogoutPath(), servletPath)) {
            request.getSession().invalidate();
            return false;
        }

        //匿名访问 放行
        HandlerMethod hm = (HandlerMethod)handler;
        AnonymousAccess anonymousAccess = AnnotationUtils.getAnnotation(hm.getMethod(), AnonymousAccess.class);
        if(anonymousAccess != null){
            return true;
        }

        //校验token是否一致
        boolean isOK = checkAccessTokenOnPartSession(request);
        if(isOK){
            return true;
        }

        log.info("{} 校验AccessToken", servletPath);
        String accessToken = Optional.ofNullable(request.getHeader(SsoConst.ACCESS_TOKEN)).orElse(request.getParameter(SsoConst.ACCESS_TOKEN));
        boolean isSuccess = check(accessToken, request, response);

        String targetUrl = request.getParameter(SsoConst.REDIRECT_URL);
        if(isSuccess && StringUtils.isNotBlank(targetUrl)) {
            response.sendRedirect(targetUrl);
            return false;
        }
        return isSuccess;
    }


    /**
     * 考虑新token与已存在的token情况
     * 校验token是否一致. session中的token与query,header中的token对比.
     * @param request
     * @return
     */
    private boolean checkAccessTokenOnPartSession(HttpServletRequest request){
        //校验当前系统局部会话中,是否有用户信息
        Object sessionObject = request.getSession().getAttribute(SsoConst.SSO_USER);
        if (sessionObject != null) {
            SsoSecurityUtils.setUserInfo((SsoUserVo) sessionObject);

            SsoTokenVo ssoTokenVo = (SsoTokenVo)request.getSession().getAttribute(SsoConst.PART_SESSION_TOKEN_ATTR);
            boolean tokenIsBlank = StringUtils.isBlank(request.getParameter(SsoConst.ACCESS_TOKEN))
                                        && StringUtils.isBlank(request.getHeader(SsoConst.ACCESS_TOKEN));
            if(ssoTokenVo == null || tokenIsBlank){
                return true;
            }

            boolean isQueryTokenEqual = StringUtils.equals(ssoTokenVo.getAccessToken(), request.getParameter(SsoConst.ACCESS_TOKEN));
            boolean isHeadTokenEqual = StringUtils.equals(ssoTokenVo.getAccessToken(), request.getHeader(SsoConst.ACCESS_TOKEN));
            if(isQueryTokenEqual || isHeadTokenEqual) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验accessToken有效性,并获取用户信息放入局部会话中
     *
     * @param accessToken
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private boolean check(String accessToken, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String targetUrl = ssoProperties.getTargetUrl();
        UrlBuilder urlBuilder = UrlBuilder.of(ssoProperties.getServer() + SsoConst.SSO_LOGIN_URL + "/" + ssoProperties.getAppCode(), Charsets.UTF_8);
        if (StringUtils.isNotBlank(targetUrl)) {
            urlBuilder.addQuery(SsoConst.REDIRECT_URL, targetUrl);
        }
        String ssoLoginUrl = urlBuilder.build();

        //校验accessToken
        if (StringUtils.isBlank(accessToken)) {
            if (WebUtil.isAjax(request) || WebUtil.isContentTypeJson(request)) {
                log.info("缺少:" + SsoConst.ACCESS_TOKEN);
                ApiResult apiResult = ApiResult.failure().message("未登录").data(ssoLoginUrl).code(401);
                output(response, FastJsonUtil.collectToString(apiResult));
            } else {
                response.sendRedirect(ssoLoginUrl);
            }
            return false;
        }

        //todo 该接口包含了两个动作. 后期优化可以考虑分开.
        //1.校验token有效性. 2.获取用户信息
        SsoUserVo ssoUserVo = ssoRemoteHelper.remoteCheck(accessToken, true);
        if(ssoUserVo == null) {
            log.error("获取用户信息失败");
            if (WebUtil.isAjax(request) || WebUtil.isContentTypeJson(request)) {
                ApiResult apiResult = ApiResult.failure().message("未登录").data(ssoLoginUrl).code(401);
                output(response, FastJsonUtil.collectToString(apiResult));
            } else {
                response.sendRedirect(ssoLoginUrl);
            }
            return false;
        }

        //放入本系统会话中.
        request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
        //保存用户信息到线程中
        SsoSecurityUtils.setUserInfo(ssoUserVo);

        //保存token信息到会话
        SsoTokenVo ssoTokenVo = new SsoTokenVo();
        ssoTokenVo.setAccessToken(accessToken);
        request.getSession().setAttribute(PART_SESSION_TOKEN_ATTR, ssoTokenVo);
        return true;
    }

    private void output(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().println(message);
    }
}
