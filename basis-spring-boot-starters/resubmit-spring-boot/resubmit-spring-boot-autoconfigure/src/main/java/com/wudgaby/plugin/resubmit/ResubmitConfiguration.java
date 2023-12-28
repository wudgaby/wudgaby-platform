package com.wudgaby.plugin.resubmit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/6/289:26
 * @Desc :  redis 配置
 */
@Configuration
@ConditionalOnWebApplication
public class ResubmitConfiguration {
    @Bean
    @ConditionalOnMissingBean(ResubmitService.class)
    public ResubmitService memoryResubmitService() {
        return new MemoryResubmitService();
    }

    @Bean
    @ConditionalOnMissingBean(RepeatSubmitAspect.class)
    @ConditionalOnProperty(name = "resubmit.enabled", havingValue = "true", matchIfMissing = true)
    public RepeatSubmitAspect repeatSubmitAspect(@Autowired(required = false) ResubmitService resubmitService) {
        return new RepeatSubmitAspect(resubmitService);
    }
}
