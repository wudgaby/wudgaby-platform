package com.wudgaby.platform.webcore.filter;

import cn.hutool.core.util.IdUtil;
import com.wudgaby.platform.core.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : TraceFilter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2022.02.23
 * @Desc :  请求追踪
 */
@Slf4j
public class TraceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig){
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //链路追踪
        String headerRequestId = httpServletRequest.getHeader(SystemConstant.HEADER_X_REQUEST_ID);
        String traceId = StringUtils.isBlank(headerRequestId) ? IdUtil.fastSimpleUUID() : headerRequestId;
        MDC.put(SystemConstant.MDC_TRACE_ID, traceId);
        MDC.put(SystemConstant.MDC_REQ_PATH, httpServletRequest.getRequestURI());

        chain.doFilter(request,response);

        httpServletResponse.setHeader(SystemConstant.HEADER_X_RESP_ID, traceId);
    }

    @Override
    public void destroy() {

    }
}
