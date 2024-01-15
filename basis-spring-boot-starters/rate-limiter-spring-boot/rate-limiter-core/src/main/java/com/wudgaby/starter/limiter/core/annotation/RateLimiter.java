package com.wudgaby.starter.limiter.core.annotation;

import com.wudgaby.starter.limiter.core.LimitType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/30 20:56
 * @Desc :
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 支持el表达式, ${#username+'login'} .默认key GLOBLE
     */
    String[] key() default {};

    /**
     * 时间单位里许可数
     */
    long permits() default 10;

    /**
     * 时间单位
     */
    long timeout() default 1;
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 每次获取许可数
     * 判断是否可以在指定的时间内获得许可，或者在超时期间内未获得许可的话，立即返回false。
     * guava使用
     * @return
     */
    int acquire() default 1;
    long acquireTimeout() default 1;
    TimeUnit acquireTimeUnit() default TimeUnit.SECONDS;

    /**
     * 限制类型
     * @return
     */
    LimitType limitType() default LimitType.CUSTOM;
}
