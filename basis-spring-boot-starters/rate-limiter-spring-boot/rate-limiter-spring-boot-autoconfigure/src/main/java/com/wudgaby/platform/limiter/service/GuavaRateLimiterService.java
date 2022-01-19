package com.wudgaby.platform.limiter.service;

import com.wudgaby.platform.limiter.core.annotation.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : GuavaRateLimiterService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/30 23:48
 * @Desc :
 */
@Slf4j
public class GuavaRateLimiterService extends RateLimiterService{
    private final Map<String, com.google.common.util.concurrent.RateLimiter> counterStore = new ConcurrentHashMap<>(128);

    @SneakyThrows
    @Override
    public boolean isAllowed(RateLimiter rateLimiter, Method method, Object[] args) {
        List<String> evalResultList = getKeyExpressionResultList(rateLimiter, method, args);
        for(String evalResult : evalResultList){
            com.google.common.util.concurrent.RateLimiter guavaRateLimiter = getRateLimiter(evalResult, rateLimiter.permits(), rateLimiter.timeout(), rateLimiter.timeUnit());
            boolean allowed = guavaRateLimiter.tryAcquire(rateLimiter.acquire(), rateLimiter.acquireTimeout(), rateLimiter.acquireTimeUnit());
            log.info("{}, {}, {}, {}, {}", evalResult, rateLimiter.permits(), rateLimiter.acquire(), allowed, guavaRateLimiter.getRate());
            if(!allowed){
                return false;
            }
        }
        return true;
    }

    public com.google.common.util.concurrent.RateLimiter getRateLimiter(String key, long permits, long timeout, TimeUnit timeUnit) {
        com.google.common.util.concurrent.RateLimiter counter = counterStore.get(key);
        if (counter != null) {
            return counter;
        }
        return counterStore.computeIfAbsent(key, k -> createRateLimiter(permits, timeout, timeUnit));
    }

    private com.google.common.util.concurrent.RateLimiter createRateLimiter(long permits, long timeout, TimeUnit timeUnit){
        long seconds = timeUnit.toSeconds(timeout);
        long permitsPerSecond;

        if (seconds > 0) {
            permitsPerSecond = permits / seconds;
        } else {
            permitsPerSecond = permits / 1_000;
        }
        return com.google.common.util.concurrent.RateLimiter.create(permitsPerSecond);
    }
}
