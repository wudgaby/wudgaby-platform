package com.wudgaby.ipaccess.config;

import com.wudgaby.ipaccess.IpManage;
import com.wudgaby.ipaccess.RedisCacheIpManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 17:19
 * @desc :
 */
@Slf4j
@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class RedisLettuceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(IpManage.class)
    @ConditionalOnBean(RedisTemplate.class)
    public IpManage redisCacheIpManage(RedisTemplate redisTemplate){
        log.info("init redisCacheIpManage");
        return new RedisCacheIpManage(redisTemplate);
    }
}
