package com.wudgaby.platform.limiter.service;

import com.wudgaby.platform.limiter.core.annotation.RateLimiter;

import java.lang.reflect.Method;

/**
 * @ClassName : RateLimiterService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/30 20:56
 * @Desc :   TODO
 */
public interface RateLimiterService {
    boolean isAllowed(RateLimiter rateLimiter, Method method, Object[] args);
}
