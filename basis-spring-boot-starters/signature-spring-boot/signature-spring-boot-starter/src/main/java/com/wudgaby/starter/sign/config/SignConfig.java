package com.wudgaby.starter.sign.config;

import com.wudgaby.sign.supoort.RequestCachingFilter;
import com.wudgaby.starter.redis.support.RedisSupport;
import com.wudgaby.starter.sign.aspect.SignatureAspect;
import com.wudgaby.starter.sign.service.DefaultSignatureService;
import com.wudgaby.starter.sign.service.SignatureService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/17 0:35
 * @Desc :   
 */
@Configuration
public class SignConfig {

    @Bean
    @ConditionalOnMissingBean(SignatureService.class)
    public SignatureService signatureService(){
        return new DefaultSignatureService();
    }

    @Bean
    @ConditionalOnMissingBean(SignatureAspect.class)
    public SignatureAspect signatureAspect(SignatureService signatureService, RedisSupport redisSupport){
        return new SignatureAspect(signatureService, redisSupport);
    }

    @Bean
    public RequestCachingFilter requestCachingFilter() {
        return new RequestCachingFilter();
    }

    @Bean
    public FilterRegistrationBean requestCachingFilterRegistration(RequestCachingFilter requestCachingFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(requestCachingFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
