package com.wudgaby.platform.limiter.service;

import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wudgaby.platform.core.util.ExpressionUtils;
import com.wudgaby.platform.limiter.core.LimitType;
import com.wudgaby.platform.limiter.core.annotation.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : GuavaRateLimiterService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/30 23:48
 * @Desc :   TODO
 */
@Slf4j
public class GuavaRateLimiterService implements RateLimiterService{
    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final Map<String, com.google.common.util.concurrent.RateLimiter> counterStore = new ConcurrentHashMap<>(128);

    @SneakyThrows
    @Override
    public boolean isAllowed(RateLimiter rateLimiter, Method method, Object[] args) {
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

            com.google.common.util.concurrent.RateLimiter guavaRateLimiter = getRateLimiter(keyExpress, rateLimiter.permits(), rateLimiter.timeout(), rateLimiter.timeUnit());
            boolean allowed = guavaRateLimiter.tryAcquire(rateLimiter.acquire(), rateLimiter.acquireTimeout(), rateLimiter.acquireTimeUnit());
            log.info("{}, {}, {}, {}, {}", keyExpress, rateLimiter.permits(), rateLimiter.acquire(), allowed, guavaRateLimiter.getRate());
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
