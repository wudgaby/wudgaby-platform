package com.wudgaby.platform.limiter.configuration;

import com.wudgaby.platform.limiter.service.RateLimiterService;
import com.wudgaby.platform.limiter.service.RedisRateLimiterService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@ConditionalOnClass(RedisTemplate.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RateLimiterAutoConfiguration {
    @Bean
    @ConditionalOnClass(RedisScript.class)
    public RedisScript redisRequestRateLimiterScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/request_rate_limiter.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

    @Bean
    @Primary
    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnMissingBean(RateLimiterService.class)
    public RateLimiterService redisRateLimiterService(RedisTemplate redisTemplate, RedisScript<List<Long>> redisScript) {
        return new RedisRateLimiterService(redisTemplate, redisScript);
    }
}
