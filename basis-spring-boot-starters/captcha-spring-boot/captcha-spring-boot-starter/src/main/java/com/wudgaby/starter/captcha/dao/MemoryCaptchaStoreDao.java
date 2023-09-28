package com.wudgaby.starter.captcha.dao;

import cn.hutool.core.lang.UUID;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wudgaby.starter.captcha.core.dao.CaptchaStoreDao;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 14:36
 * @desc : 本地内存存储
 */
public class MemoryCaptchaStoreDao implements CaptchaStoreDao {
    private static final LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(8)
            //设置写缓存后8秒钟过期
            .expireAfterWrite(5, TimeUnit.MINUTES)
            //设置缓存容器的初始容量为10
            .initialCapacity(10)
            //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(10_000)
            .build(new CacheLoader<String, String>() {
                @Override
                @Nonnull
                public String load(@Nonnull String key){
                    return "";
                }
            });

    @Override
    public String save(String prefix, String data) {
        String key = UUID.fastUUID().toString(true);
        loadingCache.put(prefix + key, data);
        return key;
    }

    @Override
    public String save(String prefix, String data, long second) {
        return save(prefix, data);
    }

    @Override
    public Optional<String> get(String prefix, String key) {
        return Optional.ofNullable(loadingCache.getUnchecked(prefix + key));
    }

    @Override
    public void clear(String prefix, String key) {
        loadingCache.invalidate(prefix + key);
    }
}
