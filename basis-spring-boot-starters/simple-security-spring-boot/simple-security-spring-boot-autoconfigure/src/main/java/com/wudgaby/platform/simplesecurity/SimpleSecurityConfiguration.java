package com.wudgaby.platform.simplesecurity;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        return new SimpleSecurityInterceptor(simpleSecurityService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(simpleAuthenInterceptor())
                .addPathPatterns("/**");

        registry.addInterceptor(simpleSecurityInterceptor(null))
                .addPathPatterns("/**");
    }
}
