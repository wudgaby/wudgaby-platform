package com.wudgaby.starter.limiter.service;

import com.wudgaby.starter.limiter.core.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/30 23:49
 * @Desc :   
 */
@Slf4j
@RequiredArgsConstructor
public class RedisRateLimiterService extends RateLimiterService{
    private final RedisTemplate redisTemplate;
    private final RedisScript<List<Long>> script;

    @SneakyThrows
    @Override
    public boolean isAllowed(@NonNull RateLimiter rateLimiter, Method method, Object[] args){
        List<String> evalResultList = getKeyExpressionResultList(rateLimiter, method, args);
        for(String evalResult : evalResultList) {
            List<String> redisKeys = getRedisKeys(evalResult);

            // How many requests per second do you want a user to be allowed to do?
            //令牌桶每秒填充平均速率。
            long replenishRate = getPermitsPerSecond(rateLimiter.permits(), rateLimiter.timeout(), rateLimiter.timeUnit());

            // How much bursting do you want to allow?
            // 令牌桶总容量
            long burstCapacity = rateLimiter.permits();

            List<String> scriptArgs = Arrays.asList(replenishRate + "", burstCapacity + "", Instant.now().getEpochSecond() + "", rateLimiter.acquire() + "");
            ArrayList<Long> results = (ArrayList<Long>)this.redisTemplate.execute(this.script, redisKeys, scriptArgs.toArray());
            boolean allowed = results.get(0) == 1L;
            Long tokensLeft = results.get(1);
            log.info("redis : {}, {}, {}, {}, {}", evalResult, rateLimiter.permits(), rateLimiter.acquire(), tokensLeft, allowed);

            if(!allowed){
                return false;
            }
        }
        return true;
    }

    private long getPermitsPerSecond(long permits, long timeout, TimeUnit timeUnit){
        long seconds = timeUnit.toSeconds(timeout);
        long permitsPerSecond;

        if (seconds > 0) {
            permitsPerSecond = permits / seconds;
        } else {
            permitsPerSecond = permits / 1_000;
        }
        return permitsPerSecond;
    }

    private List<String> getRedisKeys(String id) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "request_rate_limiter.{" + id;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }
}
