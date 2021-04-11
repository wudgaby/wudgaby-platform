package com.wudgaby.platform.limiter.service;

import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.util.ExpressionUtils;
import com.wudgaby.platform.limiter.core.LimitType;
import com.wudgaby.platform.limiter.core.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : RedisRateLimiterService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/30 23:49
 * @Desc :   TODO
 */
@Slf4j
@RequiredArgsConstructor
public class RedisRateLimiterService implements RateLimiterService{
    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final RedisTemplate redisTemplate;
    private final RedisScript<List<Long>> script;

    @SneakyThrows
    @Override
    public boolean isAllowed(@NonNull RateLimiter rateLimiter, Method method, Object[] args){
        List<String> keyExpressionList;
        LimitType limitType = rateLimiter.limitType();
        if(limitType == LimitType.IP){
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            keyExpressionList = Lists.newArrayList(ServletUtil.getClientIP(httpServletRequest));
        }else{
            keyExpressionList = Lists.newArrayList(rateLimiter.key());
            if (keyExpressionList.isEmpty()) {
                keyExpressionList.add("GLOBAL");
            }
        }

        String[] names = new String[0];
        if(method != null){
            names = nameDiscoverer.getParameterNames(method);
        }

        for(String keyExpress : keyExpressionList){
            if (keyExpress.contains("${")) {
                Map<String, Object> params = Maps.newHashMapWithExpectedSize(8);
                //params.put("user", Authentication.current().map(Authentication::getUser).orElse(null));
                for (int i = 0; i < args.length; i++) {
                    params.put(names.length > i ? names[i] : "arg" + i, args[i]);
                    params.put("arg" + i, args[i]);

                }
                keyExpress = ExpressionUtils.analytical(keyExpress, params, "spel");
            }
            if(log.isDebugEnabled()){
                log.debug("do rate limiter:[{}]. ", keyExpress);
            }

            List<String> redisKeys = getRedisKeys(keyExpress);

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
            log.info("redis : {}, {}, {}, {}, {}", keyExpress, rateLimiter.permits(), rateLimiter.acquire(), tokensLeft, allowed);

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
