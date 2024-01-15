package com.wudgaby.starter.ipaccess.config;

import com.wudgaby.starter.ipaccess.IpManage;
import com.wudgaby.starter.ipaccess.MemCacheIpManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 17:19
 * @desc :
 */
@Slf4j
@Configuration
@ConditionalOnMissingClass("org.springframework.data.redis.core.RedisTemplate")
public class CaffeineAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(IpManage.class)
    public IpManage memCacheIpManage(){
        log.info("init memCacheIpManage");
        return new MemCacheIpManage();
    }

}
