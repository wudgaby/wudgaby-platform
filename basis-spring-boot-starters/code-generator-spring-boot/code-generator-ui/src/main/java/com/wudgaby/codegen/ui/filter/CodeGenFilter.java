package com.wudgaby.codegen.ui.filter;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.wudgaby.codegen.ui.controller.GeneratorCodeController;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName : CodeGenFilter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/3 21:33
 * @Desc :   TODO
 */
@Slf4j
public class CodeGenFilter implements Filter {
    private List<String> urlFilters = Lists.newArrayList();
    private boolean enabled;

    public CodeGenFilter(boolean enabled){
        this.enabled = enabled;
    }

    /**
     * spring的路径匹配器
     */
    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urlFilters.add(GeneratorCodeController.ROOT_PATH + "/**");
        urlFilters.add("/codegen.html");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(!enabled){
            for(String excludeUrl : urlFilters){
                if(pathMatcher.match(excludeUrl, request.getRequestURI())){
                    log.info("{} 未开放.", excludeUrl);
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding(Charsets.UTF_8.name());
                    response.getWriter().write(FastJsonUtil.collectToString(ApiResult.failure().message("未开放")));
                    return;
                }
            }
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
