package com.wudgaby.redis.starter.config;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/3/21 0021 10:11
 * @desc :
 */
public class CustomRedisCacheManager extends RedisCacheManager {
    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    /**
     * 针对@Cacheable设置缓存过期时间
     * e.g. @Cacheable("key#3600")
     * @param name
     * @param cacheConfig
     * @return
     */
    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        String[] array = StringUtils.delimitedListToStringArray(name, "#");
        name = array[0];
        // 解析TTL
        if (array.length > 1) {
            long ttl = Long.parseLong(array[1]);
            // 注意单位我此处用的是秒，而非毫秒
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
        }
        return super.createRedisCache(name, cacheConfig);
    }
}
