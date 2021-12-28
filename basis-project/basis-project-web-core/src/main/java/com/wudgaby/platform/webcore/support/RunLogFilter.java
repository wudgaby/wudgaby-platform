package com.wudgaby.platform.webcore.support;

import cn.hutool.core.lang.UUID;
import cn.hutool.extra.servlet.ServletUtil;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.platform.core.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StopWatch;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName : RunLogFilter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019-07-31
 * @Desc :  执行时间过滤器
 */
@Slf4j
public class RunLogFilter implements Filter {
    /**
     * 最大运行时间毫秒
     */
    private static final long MAX_RUNNING_MS = 1000;

    /**
     * 排除的url
     */
    private ExcludeRegistry excludeRegistry;

    /**
     * spring的路径匹配器
     */
    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig){
        log.info("初始化排除地址列表");
        excludeRegistry = ExcludeRegistry.ofStaticResource();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String reqURI = httpServletRequest.getRequestURI();
        for(String excludeUrl : excludeRegistry.getAllExcludePatterns()){
            if(pathMatcher.match(excludeUrl, reqURI)){
                chain.doFilter(request,response);
                return;
            }
        }

        //链路追踪
        String headerRequestId = httpServletRequest.getHeader(SystemConstant.HEADER_X_REQUEST_ID);
        MDC.put(SystemConstant.MDC_REQUEST_ID, StringUtils.isBlank(headerRequestId) ? UUID.fastUUID().toString(true) : headerRequestId);

        log.info("请求开始. 请求IP: <{}> <{}> <{}>", ServletUtil.getClientIP(httpServletRequest), httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        chain.doFilter(request,response);

        stopWatch.stop();
        String tip = "";
        if(stopWatch.getTotalTimeMillis() > MAX_RUNNING_MS){
            tip = " [优化] ";
        }
        log.info("请求结束.{} 请求IP: <{}> <{}> <{}> 执行时间: {} MS", tip, ServletUtil.getClientIP(httpServletRequest), httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), stopWatch.getTotalTimeMillis());
    }

    @Override
    public void destroy() {

    }
}
