package com.wudgaby.plugin.resubmit;

import com.wudgaby.redis.api.RedisSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : SecureRegistry
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/6/289:26
 * @Desc :  redis 配置
 */
@Configuration
@ConditionalOnWebApplication
public class ResubmitConfiguration {
    @Bean
    @ConditionalOnMissingBean(RepeatSubmitAspect.class)
    @ConditionalOnProperty(name = "resubmit.enabled", havingValue = "true", matchIfMissing = true)
    public RepeatSubmitAspect repeatSubmitAspect(@Autowired(required = false) RedisSupport redisSupport, @Autowired(required = false) LoginUserService loginUserService) {
        return new RepeatSubmitAspect(redisSupport, loginUserService);
    }
}
