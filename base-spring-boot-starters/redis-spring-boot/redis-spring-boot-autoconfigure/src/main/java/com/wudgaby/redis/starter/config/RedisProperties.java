package com.wudgaby.redis.starter.config;

import com.wudgaby.redis.starter.enums.RedisConvertType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : RedisProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/16 14:38
 * @Desc :   TODO
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private RedisConvertType keyConvert = RedisConvertType.STRING;
    private RedisConvertType valueConvert = RedisConvertType.FASTJSON;
}
