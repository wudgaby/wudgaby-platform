package com.wudgaby.platform.sso.server.interceptor;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.sso.core.exception.SsoException;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : WebExceptionResolver
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:40
 * @Desc :   统一异常
 */
@Slf4j
@Component
public class WebExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {

        log.error(ex.getMessage(), ex);

        HandlerMethod method = (HandlerMethod)handler;
        boolean isJson = method.getMethodAnnotation(ResponseBody.class) != null
                            || method.getBeanType().getAnnotation(RestController.class) != null;

        // error result
        ApiResult errorResult;
        if (ex instanceof SsoException) {
            errorResult = ApiResult.failure().message(ex.getMessage());
        } else {
            errorResult = ApiResult.failure().message("服务器出错啦.");
        }

        // response
        ModelAndView mv = new ModelAndView();
        if (isJson) {
            try {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().println(FastJsonUtil.collectToString(errorResult));
                response.getWriter().flush();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            mv.addObject("exceptionMsg", errorResult.getMessage());
            mv.setViewName("/common/500");
        }
        return mv;
    }
}