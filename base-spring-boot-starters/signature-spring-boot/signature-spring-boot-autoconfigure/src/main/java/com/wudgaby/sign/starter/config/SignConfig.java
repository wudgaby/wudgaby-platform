package com.wudgaby.sign.starter.config;

import com.wudgaby.redis.api.RedisSupport;
import com.wudgaby.sign.starter.aspect.SignatureAspect;
import com.wudgaby.sign.starter.service.DefaultSignatureService;
import com.wudgaby.sign.starter.service.SignatureService;
import com.wudgaby.sign.supoort.RequestCachingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : SignConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/17 0:35
 * @Desc :   TODO
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
