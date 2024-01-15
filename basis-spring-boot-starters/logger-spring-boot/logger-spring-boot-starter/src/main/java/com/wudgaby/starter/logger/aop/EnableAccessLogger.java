package com.wudgaby.starter.logger.aop;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/4 14:45
 * @Desc :
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration(AopAccessLoggerSupportAutoConfiguration.class)
public @interface EnableAccessLogger {
}
