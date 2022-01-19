package com.wudgaby.platform.limiter.configuration;

import com.wudgaby.platform.limiter.aop.RateLimiterAopAdvisor;
import com.wudgaby.platform.limiter.service.GuavaRateLimiterService;
import com.wudgaby.platform.limiter.service.RateLimiterService;
import com.wudgaby.platform.limiter.service.RedisRateLimiterService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

/**
 * @ClassName : RateLimiterAutoConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/30 22:40
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
