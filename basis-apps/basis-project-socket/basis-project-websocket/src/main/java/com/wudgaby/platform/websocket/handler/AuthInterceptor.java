package com.wudgaby.platform.websocket.handler;

import com.wudgaby.platform.websocket.consts.SysConsts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/1 18:21
 * @Desc :
 */
@Slf4j
@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object object = request.getSession().getAttribute(SysConsts.SESSION_USER);
        if(object == null){
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
