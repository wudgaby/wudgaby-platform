package com.wudgaby.plugin.resubmit;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/11/14 18:22
 * @Desc :   
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 提示内容
     */
    String value() default "请不要重复提交";

    /**
     * 重复提交间隔时间. 只支持redis. 不支持guava cache
     */
    int expires() default 1;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
