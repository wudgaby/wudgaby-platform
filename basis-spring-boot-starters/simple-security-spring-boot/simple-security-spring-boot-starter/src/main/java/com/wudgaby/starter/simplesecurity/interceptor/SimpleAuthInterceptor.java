package com.wudgaby.starter.simplesecurity.interceptor;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.SecurityConst;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.starter.simplesecurity.annotations.AnonymousAccess;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author wudgaby
 */
@Slf4j
@AllArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class SimpleAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isPermit = checkAuthentication(handler);
        if(isPermit){
            return true;
        }

        HttpSession httpSession = request.getSession();
        UserInfo loginUser = (UserInfo) httpSession.getAttribute(SecurityConst.SESSION_LOGGED_USER);
        if(loginUser == null) {
            //未登录
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(JacksonUtil.serialize(ApiResult.failure("请先登录.")));
            response.getWriter().flush();
            return false;
        }

        SecurityUtils.setCurrentUser(loginUser);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    /**
     * 检测是否放行
     * @param handler
     * @return
     */
    public static boolean checkAuthentication(Object handler){
        //静态资源请求
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        //校验是否放行
        HandlerMethod hm = (HandlerMethod) handler;
        if(hm.getBeanType() == BasicErrorController.class) {
            return true;
        }

        AnonymousAccess anonymousAccessType = AnnotationUtils.getAnnotation(hm.getBeanType(), AnonymousAccess.class);
        AnonymousAccess anonymousAccessMethod = AnnotationUtils.getAnnotation(hm.getMethod(), AnonymousAccess.class);

        //匿名访问 放行
        return anonymousAccessType != null || anonymousAccessMethod != null;
    }
}
