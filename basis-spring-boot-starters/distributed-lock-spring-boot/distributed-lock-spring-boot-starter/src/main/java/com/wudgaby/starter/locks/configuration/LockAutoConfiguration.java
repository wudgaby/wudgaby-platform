package com.wudgaby.starter.locks.configuration;

import com.wudgaby.starter.locks.aspect.LockAspect;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/26 9:10
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
