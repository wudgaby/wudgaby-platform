package com.wudgaby.starter.data.security.sensitive.config;


import com.wudgaby.starter.data.security.sensitive.support.SensitiveResponseBodyAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wudgaby
 */
@Configuration
public class SensitiveConfig {
    @Bean
    @ConditionalOnMissingBean
    public SensitiveResponseBodyAdvice sensitiveResponseBodyAdvice(){
        return new SensitiveResponseBodyAdvice();
    }
}
