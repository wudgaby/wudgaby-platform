package com.wudgaby.starter.locks.api.annotation;

import com.wudgaby.starter.locks.api.enums.LockType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/25 15:08
 * @Desc :   
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    LockType lockType() default LockType.REENTRANT;

    /**
     * 锁key
     *
     * @return
     */
    String[] keys() default {};

    /**
     * 是否异步
     * @return
     */
    //boolean async() default false;

    /**
     * 获取锁等待时间.
     * 为0时,会一直等待,直到拿到锁.
     *
     * @return
     */
    int lockWait() default 0;

    /**
     * 锁持有时间, 超过将自动解锁
     *
     * @return
     */
    int timeout() default 30;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
