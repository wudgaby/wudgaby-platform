package com.wudgaby.starter.simplesecurity.interceptor;

import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.springext.RequestMatcher;
import com.wudgaby.platform.springext.RequestMatcherCreator;
import com.wudgaby.platform.utils.ExpressContext;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.starter.simplesecurity.LogicType;
import com.wudgaby.starter.simplesecurity.annotations.AuthPermit;
import com.wudgaby.starter.simplesecurity.service.SimpleSecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author wudgaby
 */
@Slf4j
@RequiredArgsConstructor
@Order
public class SimpleSecurityInterceptor implements HandlerInterceptor, ApplicationContextAware {
    private final SimpleSecurityService simpleSecurityService;
    private final RequestMatcherCreator requestMatcherCreator;
    private ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isPermit = checkSecurity(handler);
        if(isPermit){
            return true;
        }

        //校验是否放行
        HandlerMethod hm = (HandlerMethod) handler;
        AuthPermit authPermit = AnnotationUtils.getAnnotation(hm.getMethod(), AuthPermit.class);
        //不需要权限
        if (authPermit == null) {
            return true;
        }

        //管理员可以访问所有
        if(simpleSecurityService.checkAdmin()){
            return true;
        }

        //未设置值时,拒绝访问
        boolean isAuthEmpty = StringUtils.isBlank(authPermit.value())
                                        && ArrayUtils.isEmpty(authPermit.roles())
                                        && ArrayUtils.isEmpty(authPermit.perms());
        if(isAuthEmpty && !authPermit.enableUrl()){
            forbidden(response);
            return false;
        }

        //校验spEL表达式
        boolean isPass = checkPermissionForSpEL(authPermit, Maps.newHashMap(request.getParameterMap()));
        if(!isPass) {
            forbidden(response);
            return false;
        }

        //校验角色码
        isPass = checkRole(authPermit);
        if(!isPass) {
            forbidden(response);
            return false;
        }

        //校验权限码
        isPass = checkPermission(authPermit);
        if(!isPass) {
            forbidden(response);
            return false;
        }

        //校验url鉴权
        if(authPermit.enableUrl()){
            Set<RequestMatcher> requestMatchers = requestMatcherCreator.convertToRequestMatcher(simpleSecurityService.getMetaResourceList());
            RequestMatcher reqMatcher = requestMatchers.stream().filter(requestMatcher -> requestMatcher.matches(request)).findAny()
                                                                        .orElseThrow(() -> new AccessDeniedException("无权限访问该接口"));
            if(reqMatcher == null){
                forbidden(response);
                return false;
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void forbidden(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JacksonUtil.serialize(ApiResult.failure("无权限访问.")));
        response.getWriter().flush();
    }

    private boolean checkPermissionForSpEL(AuthPermit authPermit, Map<String, Object> paramMap) {
        boolean isPass = true;
        if(StringUtils.isNotBlank(authPermit.value())) {
            ExpressContext expressContext = new ExpressContext(applicationContext, paramMap);
            return expressContext.getBooleanValue(authPermit.value());
        }
        return isPass;
    }

    private boolean checkRole(AuthPermit authPermit){
        boolean isPass = true;
        if(ArrayUtils.isNotEmpty(authPermit.roles())){
            if(authPermit.logicType() == LogicType.OR) {
                isPass = simpleSecurityService.hasAnyRole(authPermit.roles());
            } else if(authPermit.logicType() == LogicType.AND) {
                isPass = simpleSecurityService.hasAllRole(authPermit.roles());
            } else {
                throw new RuntimeException("AuthPermit注解逻辑枚举错误");
            }
        }
        return isPass;
    }

    private boolean checkPermission(AuthPermit authPermit){
        boolean isPass = true;
        if(ArrayUtils.isNotEmpty(authPermit.perms())){
            if(authPermit.logicType() == LogicType.OR) {
                isPass = simpleSecurityService.hasAnyPermission(authPermit.perms());
            } else if(authPermit.logicType() == LogicType.AND) {
                isPass = simpleSecurityService.hasAllPermission(authPermit.perms());
            } else {
                throw new RuntimeException("AuthPermit注解逻辑枚举错误");
            }
        }
        return isPass;
    }

    /**
     * 是否需要权限校验
     * @param handler
     * @return
     */
    public static boolean checkSecurity(Object handler){
        //静态资源请求
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        //校验是否放行
        HandlerMethod hm = (HandlerMethod) handler;
        if(hm.getBeanType() == BasicErrorController.class) {
            return true;
        }

        AuthPermit authPermitType = AnnotationUtils.getAnnotation(hm.getBeanType(), AuthPermit.class);
        AuthPermit authPermitMethod = AnnotationUtils.getAnnotation(hm.getMethod(), AuthPermit.class);

        //就近原则.即优先级:方法级别 > 类级别
        if (authPermitMethod != null) {
            return false;
        }

        return authPermitType == null;
    }
}
