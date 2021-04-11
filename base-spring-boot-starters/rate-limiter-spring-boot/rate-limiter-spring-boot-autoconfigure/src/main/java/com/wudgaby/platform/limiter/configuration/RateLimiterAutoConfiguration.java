package com.wudgaby.platform.limiter.configuration;

import com.wudgaby.platform.limiter.aop.RateLimiterAopAdvisor;
import com.wudgaby.platform.limiter.service.GuavaRateLimiterService;
import com.wudgaby.platform.limiter.service.RateLimiterService;
import com.wudgaby.platform.limiter.service.RedisRateLimiterService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * @ClassName : RateLimiterAutoConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/30 22:40
 * @Desc :   TODO
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RateLimiterAutoConfiguration {
    @Bean
    @SuppressWarnings("unchecked")
    public RedisScript redisRequestRateLimiterScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/request_rate_limiter.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnMissingBean(RateLimiterService.class)
    @Order(-1)
    public RateLimiterService redisRateLimiterService(StringRedisTemplate stringRedisTemplate, RedisScript<List<Long>> redisScript) {
        return new RedisRateLimiterService(stringRedisTemplate, redisScript);
    }

    @Bean
    @Order(1)
    @ConditionalOnMissingBean(RateLimiterService.class)
    public RateLimiterService guavaRateLimiterService() {
        return new GuavaRateLimiterService();
    }

    @Bean
    @ConditionalOnBean(RateLimiterService.class)
    public RateLimiterAopAdvisor redisRateLimiterAopAdvisor(RateLimiterService rateLimiterService){
        return new RateLimiterAopAdvisor(rateLimiterService);
    }
}
