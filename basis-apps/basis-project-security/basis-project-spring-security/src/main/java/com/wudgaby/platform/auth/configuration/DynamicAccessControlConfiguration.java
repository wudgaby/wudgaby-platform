package com.wudgaby.platform.auth.configuration;

import com.wudgaby.platform.auth.extend.dynamic.DynamicFilterInvocationSecurityMetadataSource;
import com.wudgaby.platform.auth.extend.dynamic.RequestMatcherCreator;
import com.wudgaby.platform.auth.extend.dynamic.UrlAccessDecisionManager;
import com.wudgaby.platform.auth.extend.dynamic.UrlFilterInvocationSecurityMetadataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.stream.Collectors;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/10 10:03
 * @Desc :   动态权限组件配置
 */
@Configuration
public class DynamicAccessControlConfiguration {
    /**
     *  RequestMatcher 生成器
     * @return RequestMatcher
     */
    @Bean
    public RequestMatcherCreator requestMatcherCreator() {
        return metaResources -> metaResources.stream()
                .map(metaResource -> new AntPathRequestMatcher(metaResource.getPattern(), metaResource.getMethod()))
                .collect(Collectors.toSet());
    }

    /**MetaResource.java
     * 元数据加载器
     *
     * @return dynamicFilterInvocationSecurityMetadataSource
     */
    @Bean
    @Primary
    public FilterInvocationSecurityMetadataSource dynamicFilterInvocationSecurityMetadataSource() {
        return new DynamicFilterInvocationSecurityMetadataSource();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        return new UrlFilterInvocationSecurityMetadataSource();
    }

    /**
     *  角色投票器
     * @return roleVoter
     */
    @Bean
    public RoleVoter roleVoter() {
        return new RoleVoter();
    }

    /**
     *  基于肯定的访问决策器
     *
     * @param decisionVoters  AccessDecisionVoter类型的 Bean 会自动注入到 decisionVoters
     * @return affirmativeBased
     */
    @Bean
    @Primary
    public AccessDecisionManager affirmativeBased(List<AccessDecisionVoter<?>> decisionVoters) {
        return new AffirmativeBased(decisionVoters);
    }

    /**
     * 自定义 基于url + method 权限判定
     * @return
     */
    @Bean
    public AccessDecisionManager urlAccessDecisionManager() {
        return new UrlAccessDecisionManager();
    }
}
