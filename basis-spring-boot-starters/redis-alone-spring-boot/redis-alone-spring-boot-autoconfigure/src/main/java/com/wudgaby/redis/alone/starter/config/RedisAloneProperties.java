package com.wudgaby.redis.alone.starter.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2022.03.29
 * @desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisAloneProperties {
    private Map<String, RedisProperties> alones = new LinkedHashMap<>();
}
