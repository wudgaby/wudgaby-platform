package com.wudgaby.platform.simplesecurity.config;

import com.google.common.collect.Lists;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.platform.simplesecurity.DefaultSimpleSecurityServiceImpl;
import com.wudgaby.platform.simplesecurity.SimpleAuthenInterceptor;
import com.wudgaby.platform.simplesecurity.SimpleSecurityInterceptor;
import com.wudgaby.platform.simplesecurity.SimpleSecurityService;
import com.wudgaby.platform.simplesecurity.ext.RequestMatcherCreator;
import com.wudgaby.platform.simplesecurity.spring.AntPathRequestMatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Collectors;

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/10 10:19
 * @desc :
 */
@Configuration
@ConditionalOnWebApplication
public class SimpleSecurityConfiguration implements WebMvcConfigurer {
    @Bean
    @ConditionalOnMissingBean
    public SimpleSecurityService simpleSecurityService(){
        return new DefaultSimpleSecurityServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleAuthenInterceptor simpleAuthenInterceptor(){
        return new SimpleAuthenInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleSecurityInterceptor simpleSecurityInterceptor(SimpleSecurityService simpleSecurityService){
        return new SimpleSecurityInterceptor(simpleSecurityService, requestMatcherCreator());
    }

    @Bean
    public ExcludeRegistry secureRegistry() {
        ExcludeRegistry excludeRegistry = new ExcludeRegistry();
        return excludeRegistry;
    }

    @Bean
    public RequestMatcherCreator requestMatcherCreator() {
        return metaResources -> metaResources.stream()
                .map(metaResource -> new AntPathRequestMatcher(metaResource.getPattern(), metaResource.getMethod()))
                .collect(Collectors.toSet());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(simpleAuthenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(Lists.newArrayList(secureRegistry().getDefaultExcludePatterns()))
                .excludePathPatterns(Lists.newArrayList(secureRegistry().getExcludePatterns()));

        registry.addInterceptor(simpleSecurityInterceptor(null))
                .addPathPatterns("/**")
                .excludePathPatterns(Lists.newArrayList(secureRegistry().getDefaultExcludePatterns()))
                .excludePathPatterns(Lists.newArrayList(secureRegistry().getExcludePatterns()));
    }
}
