package com.wudgaby.starter.tenant.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.starter.tenant.config.TenantProperties;
import com.wudgaby.starter.tenant.context.TenantContextHolder;
import com.wudgaby.starter.tenant.service.TenantFrameworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * 多租户 Security Web 过滤器
 * 1. 如果是登陆的用户，校验是否有权限访问该租户，避免越权问题。
 * 2. 如果请求未带租户的编号，检查是否是忽略的 URL，否则也不允许访问。
 * 3. 校验租户是合法，例如说被禁用、到期
 *
 * @author 芋道源码
 */
@Slf4j
@RequiredArgsConstructor
public class TenantSecurityWebFilter extends OncePerRequestFilter {

    private final TenantProperties tenantProperties;
    private final TenantFrameworkService tenantFrameworkService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    @SuppressWarnings("deprecation")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Long tenantId = TenantContextHolder.getTenantId();
        // 1. 登陆的用户，校验是否有权限访问该租户，避免越权问题。
        UserInfo user = SecurityUtils.getCurrentUser();
        if (user != null) {
            // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
            if (tenantId == null) {
                tenantId = user.getTenantId();
                TenantContextHolder.setTenantId(tenantId);
            // 如果传递了租户编号，则进行比对租户编号，避免越权问题
            } else if (!Objects.equals(user.getTenantId(), TenantContextHolder.getTenantId())) {
                log.error("[doFilterInternal][租户({}) User({}/{}) 越权访问租户({}) URL({}/{})]",
                        user.getTenantId(), user.getId(), user.getUserType(),
                        TenantContextHolder.getTenantId(), request.getRequestURI(), request.getMethod());

                ServletUtil.write(response, JacksonUtil.serialize(ApiResult.failure("您无权访问该租户的数据")), MediaType.APPLICATION_JSON_UTF8_VALUE);
                return;
            }
        }

        // 如果非允许忽略租户的 URL，则校验租户是否合法
        if (!isIgnoreUrl(request)) {
            // 2. 如果请求未带租户的编号，不允许访问。
            if (tenantId == null) {
                log.error("[doFilterInternal][URL({}/{}) 未传递租户编号]", request.getRequestURI(), request.getMethod());
                ServletUtil.write(response, JacksonUtil.serialize(ApiResult.failure("请求的租户标识未传递，请进行排查")), MediaType.APPLICATION_JSON_UTF8_VALUE);
                return;
            }
            // 3. 校验租户是合法，例如说被禁用、到期
            try {
                tenantFrameworkService.validTenant(tenantId);
            } catch (Throwable ex) {
                ServletUtil.write(response, JacksonUtil.serialize(ApiResult.failure(ex.getMessage())), MediaType.APPLICATION_JSON_UTF8_VALUE);
                return;
            }
        } else { // 如果是允许忽略租户的 URL，若未传递租户编号，则默认忽略租户编号，避免报错
            if (tenantId == null) {
                TenantContextHolder.setIgnore(true);
            }
        }

        // 继续过滤
        chain.doFilter(request, response);
    }

    private boolean isIgnoreUrl(HttpServletRequest request) {
        Set<String> ignoreUrls = ExcludeRegistry.ofStaticResource().getAllExcludePatterns();
        if(CollUtil.isNotEmpty(tenantProperties.getIgnoreUrls())){
            ignoreUrls.addAll(tenantProperties.getIgnoreUrls());
        }

        // 快速匹配，保证性能
        if (CollUtil.contains(ignoreUrls, request.getRequestURI())) {
            return true;
        }
        // 逐个 Ant 路径匹配
        for (String url : ignoreUrls) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }

}
