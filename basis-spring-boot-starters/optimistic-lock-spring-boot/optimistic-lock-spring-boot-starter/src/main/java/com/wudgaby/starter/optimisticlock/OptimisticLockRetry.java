package com.wudgaby.starter.optimisticlock;

import java.lang.annotation.*;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/2/22 0022 10:15
 * @desc :  乐观锁重试
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptimisticLockRetry {

    /**
     * 最大重试次数
     */
    int value() default 10;

    /**
     * 最大执行时间 ms
     */
    int maxExecuteTime() default 60 * 1000;
}
