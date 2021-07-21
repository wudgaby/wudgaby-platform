package com.wudgaby.platform.sso.sample;

import com.wudgaby.platform.webcore.support.GlobalExceptionAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @ClassName : SsoWebSampleBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/24 15:57
 * @Desc :
 */
@SpringBootApplication
@ComponentScan(value = "com.wudgaby.platform",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GlobalExceptionAdvice.class}))
public class SsoWebSampleBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SsoWebSampleBootstrap.class, args);
    }
}
