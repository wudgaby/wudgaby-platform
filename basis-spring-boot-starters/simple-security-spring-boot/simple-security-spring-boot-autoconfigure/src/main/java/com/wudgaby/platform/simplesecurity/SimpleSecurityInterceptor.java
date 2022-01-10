package com.wudgaby.platform.simplesecurity;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Order
public class SimpleSecurityInterceptor implements HandlerInterceptor {
    private final SimpleSecurityService simpleSecurityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isPermit = SimpleAuthenInterceptor.checkPermit(handler);
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

        //校验角色码
        if(ArrayUtils.isNotEmpty(authPermit.roles())){
            if(simpleSecurityService.hasRole(authPermit.roles())){
                return true;
            }

            forbidden(response);
            return false;
        }

        //校验权限码
        if(ArrayUtils.isNotEmpty(authPermit.perms())){
            if(simpleSecurityService.hasPermission(authPermit.perms())){
                return true;
            }

            forbidden(response);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private void forbidden(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(FastJsonUtil.collectToString(ApiResult.failure("无权限访问.")));
        response.getWriter().flush();
    }
}
