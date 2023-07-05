package com.wudgaby.platform.sso.core.interceptor;

import com.google.common.base.Charsets;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.annotations.AnonymousAccess;
import com.wudgaby.platform.sso.core.annotations.AuthPermit;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoRemoteHelper;
import com.wudgaby.platform.sso.core.utils.SsoSecurityUtils;
import com.wudgaby.platform.sso.core.vo.PermissionVo;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.platform.utils.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : PermissionInterceptor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/4 18:01
 * @Desc :   
 */
@Slf4j
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final SsoRemoteHelper ssoRemoteHelper;
    private final SsoProperties ssoProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        HandlerMethod hm = (HandlerMethod)handler;
        AnonymousAccess anonymousAccess = AnnotationUtils.getAnnotation(hm.getMethod(), AnonymousAccess.class);
        AuthPermit permit = AnnotationUtils.getAnnotation(hm.getMethod(), AuthPermit.class);
        //匿名访问 放行
        if(anonymousAccess != null || permit != null){
            return true;
        }

        SsoUserVo ssoUserVo = SsoSecurityUtils.getUserInfo();

        String headerSessionId = request.getHeader(SsoConst.SSO_HEADER_X_TOKEN);
        // 当前用户所有权限
        //List<PermissionVo> userPermissionList = ssoRemoteHelper.getUserResource(headerSessionId, ssoProperties.getSysCode());

        List<PermissionVo> userPermissionList = ssoRemoteHelper.getUserResource(headerSessionId, ssoUserVo.getUserId(), ssoProperties.getAppCode());

        // 过滤后的权限
        List<PermissionVo> filtered = filterPermissionList(request.getRequestURI(), request.getMethod(), userPermissionList);

        if(CollectionUtils.isEmpty(filtered)){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding(Charsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            String json = JacksonUtil.serialize(ApiResult.<String>failure().message("无权限"));
            response.getWriter().write(json);
            response.getWriter().flush();
            return false;
        }

        return true;
    }

    private List<PermissionVo> filterPermissionList(final String requestUri, final String method, List<PermissionVo> userPermissionList) {
        return userPermissionList.parallelStream().filter(permissionVo ->
            antPathMatcher.match(permissionVo.getUri(), requestUri) && method.equalsIgnoreCase(permissionVo.getMethod())
        ).collect(Collectors.toList());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //不总是会被调用.导致不被清理.
        //spring用的线程池.导致变量不能被回收和被串用
        SsoSecurityUtils.clear();
    }
}
