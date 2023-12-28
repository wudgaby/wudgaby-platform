package com.wudgaby.plugin.resubmit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/28 0028 14:39
 * @desc :
 */
public class MemoryResubmitService implements ResubmitService{
    private static final Cache<String, String> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .maximumSize(99_999)
            .build();
    @Override
    public String getLoggedUserId() {
        return "";
    }

    @Override
    public void check(String key, String message, long expiresSec) {
        String val = CACHE.getIfPresent(key);
        if (val == null) {
            CACHE.put(key, key);
        } else {
            throw new ResubmitException(message);
        }
    }
}
