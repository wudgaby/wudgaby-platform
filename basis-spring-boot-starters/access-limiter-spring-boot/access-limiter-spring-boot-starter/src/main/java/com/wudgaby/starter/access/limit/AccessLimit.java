package com.wudgaby.starter.access.limit;

import java.lang.annotation.*;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/2 0002 10:43
 * @desc :  访问限制
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    String value() default "访问太快,禁止访问.";
    /**
     * 秒
     * @return 多少秒内
     */
    long second() default 5L;

    /**
     * 最大访问次数
     * @return 最大访问次数
     */
    long maxTime() default 3L;

    /**
     * 禁用时长，单位/秒
     * @return 禁用时长
     */
    long forbiddenTime() default 120L;
}
