package com.wudgaby.sample;

import com.wudgaby.platform.security.core.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/3 0003 18:18
 * @desc :
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityUtils.set(SecurityUtils.TENANT_KEY, request.getSession().getAttribute(SecurityUtils.TENANT_KEY));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
