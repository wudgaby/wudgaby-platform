package com.wudgaby.starter.logger.aop;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * @ClassName : EnableAccessLogger
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/4 14:45
 * @Desc :   TODO
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration(AopAccessLoggerSupportAutoConfiguration.class)
public @interface EnableAccessLogger {
}
