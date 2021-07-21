package com.wudgaby.locks.starter.configuration;

import com.wudgaby.locks.starter.aspect.LockAspect;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : LockAutoConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/26 9:10
 * @Desc :
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@AutoConfigureBefore(RedissonAutoConfiguration.class)
public class LockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(LockAspect.class)
    public LockAspect lockAspect(RedissonClient redissonClient) {
        return new LockAspect(redissonClient);
    }
}
