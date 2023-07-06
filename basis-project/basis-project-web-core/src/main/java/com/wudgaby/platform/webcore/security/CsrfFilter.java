package com.wudgaby.platform.webcore.security;

import cn.hutool.json.JSON;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.result.enums.SystemResultCode;
import com.wudgaby.platform.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * @ClassName : CsrfFilter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/10/5/005 23:00
 * @Desc :   跨站请求伪造
 */
@Slf4j
public class CsrfFilter implements Filter {
    private static final String X_CSRF_TOKEN = "X-CSRF-TOKEN";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init csrf filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();

        String session_csrf_token = Optional.ofNullable(session.getAttribute(X_CSRF_TOKEN)).map(obj -> obj.toString()).orElse(null);
        if(!HttpMethod.POST.name().equalsIgnoreCase(httpServletRequest.getMethod())
                || StringUtils.isEmpty(session_csrf_token)){
            chain.doFilter(request, response);
            return;
        }

        String csrf_token = httpServletRequest.getParameter(X_CSRF_TOKEN);
        if(StringUtils.isEmpty(csrf_token)){
            csrf_token = httpServletRequest.getHeader(X_CSRF_TOKEN);
        }
        if(!StringUtils.equals(csrf_token, session_csrf_token)){
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(JacksonUtil.serialize(ApiResult.failure(SystemResultCode.TOKEN_IS_INVALID)));
            response.getWriter().flush();
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
