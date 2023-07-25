package com.wudgaby.starter.multiports;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/19 0019 14:37
 * @desc :
 */
@Slf4j
@Data
public class TrustedEndpointsFilter implements Filter {
    private int trustedPort = 0;
    private String trustedPathPrefix;
    /**
     * 过滤器前置条件
     */
    private Predicate<String> excludedPathPredicate;

    TrustedEndpointsFilter(String trustedPort, String trustedPathPrefix) {
        if (trustedPort != null && trustedPathPrefix != null) {
            this.trustedPort = Integer.valueOf(trustedPort);
            this.trustedPathPrefix = trustedPathPrefix;
        }
    }

    TrustedEndpointsFilter(String trustedPort, String trustedPathPrefix, Predicate<String> excludedPathPredicate) {
        this(trustedPort, trustedPathPrefix);
        this.excludedPathPredicate = excludedPathPredicate;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String reqURI = httpServletRequest.getRequestURI();
        if(excludedPathPredicate != null) {
            if(excludedPathPredicate.test(reqURI)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        if (this.trustedPort != 0) {
            // 通过外部端口试图访问内部接口，拒绝请求
            if (isRequestForTrustedEndpoint(servletRequest) && servletRequest.getLocalPort() != this.trustedPort) {
                log.warn("拒绝对<不受信任端口>上的<受信任端点>的请求! {}", reqURI);
                /*((ResponseFacade) servletResponse).setStatus(404);
                servletResponse.getOutputStream().close();*/
                forbidden(httpServletResponse);
                return;
            }

            // 通过内部端口试图访问外部接口，拒绝请求
            if (!isRequestForTrustedEndpoint(servletRequest) && servletRequest.getLocalPort() == this.trustedPort) {
                log.warn("拒绝对<受信任端口>上<不受信任端点>的请求! {}", reqURI);
                /*((ResponseFacade) servletResponse).setStatus(404);
                servletResponse.getOutputStream().close();*/
                forbidden(httpServletResponse);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 通过 URL 中的路径前缀来判断对应的接口是内部接口还是外部接口
     * @param servletRequest
     * @return
     */
    private boolean isRequestForTrustedEndpoint(ServletRequest servletRequest) {
        return ((HttpServletRequest) servletRequest).getRequestURI().startsWith(trustedPathPrefix);
    }

    private void forbidden(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.HTTP_NOT_FOUND);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        Map<String, Object> resultMap = Maps.newTreeMap();
        resultMap.put("code", 0);
        resultMap.put("message", "404");
        resultMap.put("data", null);
        response.getWriter().write(JSONUtil.toJsonStr(resultMap));
        response.getWriter().flush();
    }
}
