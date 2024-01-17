package com.wudgaby.sample;

import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.springext.RequestContextHolderSupport;
import com.wudgaby.platform.utils.WebServletUtil;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

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
        //静态资源请求
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        //校验是否放行
        HandlerMethod hm = (HandlerMethod) handler;
        if(hm.getBeanType() == BasicErrorController.class) {
            return true;
        }

        UserInfo userInfo = (UserInfo)RequestContextHolderSupport.getRequest().getSession().getAttribute("user");
        if(userInfo == null){
            WebServletUtil.writeJson(response, "请先登录");
            return false;
        }
        SecurityUtils.setCurrentUser(userInfo);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
