package com.wudgaby.platform.sso.core.interceptor;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.annotations.AnonymousAccess;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoRemoteHelper;
import com.wudgaby.platform.sso.core.utils.SsoSecurityUtils;
import com.wudgaby.platform.sso.core.vo.SsoTokenVo;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.wudgaby.platform.sso.core.constant.SsoConst.PART_SESSION_TOKEN_ATTR;

/**
 * @ClassName : SsoTokenInterceptor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:21
 * @Desc :
 */
@Slf4j
@RequiredArgsConstructor
public class SsoTokenInterceptor implements HandlerInterceptor {
    private final SsoProperties ssoProperties;
    private final SsoRemoteHelper ssoRemoteHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        String servletPath = request.getServletPath();
        if (StringUtils.equals(ssoProperties.getLogoutPath(), servletPath)) {
            request.getSession().invalidate();
            ApiResult apiResult = ApiResult.success().message("登出成功");
            output(response, FastJsonUtil.collectToString(apiResult));
            return false;
        }

        HandlerMethod hm = (HandlerMethod)handler;
        AnonymousAccess anonymousAccess = AnnotationUtils.getAnnotation(hm.getMethod(), AnonymousAccess.class);
        //匿名访问 放行
        if(anonymousAccess != null){
            return true;
        }

        //校验当前系统局部会话中,是否有用户信息
        Object sessionObject = request.getSession().getAttribute(SsoConst.SSO_USER);
        if (sessionObject != null) {
            SsoSecurityUtils.setUserInfo((SsoUserVo) sessionObject);

            SsoTokenVo ssoTokenVo = (SsoTokenVo)request.getSession().getAttribute(SsoConst.PART_SESSION_TOKEN_ATTR);
            boolean tokenIsBlank = StringUtils.isBlank(request.getParameter(SsoConst.SSO_HEADER_X_TOKEN));
            if(ssoTokenVo == null || tokenIsBlank){
                return true;
            }

            boolean isHeadTokenEqual = StringUtils.equals(ssoTokenVo.getAccessToken(), request.getHeader(SsoConst.SSO_HEADER_X_TOKEN));
            if(isHeadTokenEqual) {
                return true;
            }
        }

        String code = request.getParameter("code");
        //code换token
        if (StringUtils.isNotBlank(code)) {
            String sign = DigestUtils.sha1Hex(code + ssoProperties.getAppSecret());

            ApiResult tokenApiResult = ssoRemoteHelper.codeExToken(code, sign, ssoProperties.getAppSecret());
            ApiResult apiResult;
            if(ApiResult.isSuccess(tokenApiResult)){
                apiResult = ApiResult.success().data(tokenApiResult.getData());
            }else{
                apiResult = ApiResult.failure().message(tokenApiResult.getMessage());
            }
            output(response, FastJsonUtil.collectToString(apiResult));
            return false;
        }


        //去sso服务器校验有效性
        String token = request.getHeader(SsoConst.SSO_HEADER_X_TOKEN);
        SsoUserVo ssoUserVo = ssoRemoteHelper.remoteCheck(token, false);

        if (ssoUserVo == null) {
            ApiResult apiResult = ApiResult.failure().message("未登录").code(401);
            output(response, FastJsonUtil.collectToString(apiResult));
            return false;
        }

        //放入本系统会话中.
        request.getSession().setAttribute(SsoConst.SSO_USER, ssoUserVo);
        //保存用户信息到线程中
        SsoSecurityUtils.setUserInfo(ssoUserVo);

        //保存token信息到会话
        SsoTokenVo ssoTokenVo = new SsoTokenVo();
        ssoTokenVo.setAccessToken(token);
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
