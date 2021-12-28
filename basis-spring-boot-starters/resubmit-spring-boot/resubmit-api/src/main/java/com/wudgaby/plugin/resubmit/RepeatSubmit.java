package com.wudgaby.plugin.resubmit;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : RepeatSubmit
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/14 18:22
 * @Desc :   
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 提示内容
     * @return
     */
    String value();

    /**
     * 重复提交间隔时间. 只支持redis. 不支持guava cache
     * @return
     */
    int expires() default 2;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
