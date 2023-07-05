package com.wudgaby.sign.supoort;

import com.google.common.collect.Maps;
import com.wudgaby.platform.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @ClassName : RequestCachingFilter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/17 0:54
 * @Desc :   
 */
@Slf4j
public class RequestCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        if (isFirstRequest && !(request instanceof BufferedHttpServletRequest)) {
            requestToUse = new BufferedHttpServletRequest(request, 1024);
        }
        try {
            filterChain.doFilter(requestToUse, response);
        } catch (Exception e) {
            log.error("RequestCachingFilter>>>>>>>>>", e);
        } finally {
            this.printRequest(requestToUse);
            if (requestToUse instanceof BufferedHttpServletRequest) {
                ((BufferedHttpServletRequest) requestToUse).release();
            }
        }
    }

    private void printRequest(HttpServletRequest request) {
        String body = StringUtils.EMPTY;
        try {
            if (request instanceof BufferedHttpServletRequest) {
                body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("printRequest 获取body异常...", e);
        }


        Map<String, Object> requestJ = Maps.newHashMap();
        Map<String, Object> headers = Maps.newHashMap();
        Collections.list(request.getHeaderNames())
                .stream()
                .forEach(name -> headers.put(name, request.getHeader(name)));
        requestJ.put("headers", headers);
        requestJ.put("parameters", request.getParameterMap());
        requestJ.put("body", body);
        requestJ.put("remote-user", request.getRemoteUser());
        requestJ.put("remote-addr", request.getRemoteAddr());
        requestJ.put("remote-host", request.getRemoteHost());
        requestJ.put("remote-port", request.getRemotePort());
        requestJ.put("uri", request.getRequestURI());
        requestJ.put("url", request.getRequestURL());
        requestJ.put("servlet-path", request.getServletPath());
        requestJ.put("method", request.getMethod());
        requestJ.put("query", request.getQueryString());
        requestJ.put("path-info", request.getPathInfo());
        requestJ.put("context-path", request.getContextPath());

        log.info("Request-Info: " + JacksonUtil.serialize(requestJ));
    }

}