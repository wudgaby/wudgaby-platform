package com.wudgaby.platform.limiter.configuration;

import com.wudgaby.platform.limiter.aop.RateLimiterAopAdvisor;
import com.wudgaby.platform.limiter.service.GuavaRateLimiterService;
import com.wudgaby.platform.limiter.service.RateLimiterService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/30 22:40
 * @Desc :
 */
@Configuration
@AutoConfigureAfter(RateLimiterAutoConfiguration.class)
public class GuavaRateLimiterAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(RateLimiterService.class)
    public RateLimiterService rateLimiterService() {
        return new GuavaRateLimiterService();
    }

    @Bean
    @ConditionalOnBean(RateLimiterService.class)
    public RateLimiterAopAdvisor redisRateLimiterAopAdvisor(RateLimiterService rateLimiterService){
        return new RateLimiterAopAdvisor(rateLimiterService);
    }
}
