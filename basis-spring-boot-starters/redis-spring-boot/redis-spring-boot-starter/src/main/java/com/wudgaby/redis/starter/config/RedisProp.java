package com.wudgaby.redis.starter.config;

import com.wudgaby.redis.starter.enums.RedisConvertType;
import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/16 14:38
 * @Desc :
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProp {
    private RedisConvertType keyConvert = RedisConvertType.STRING;
    private RedisConvertType valueConvert = RedisConvertType.JACKSON;

    private Map<String, RedisProperties> aloneList = new LinkedHashMap<>();
}
