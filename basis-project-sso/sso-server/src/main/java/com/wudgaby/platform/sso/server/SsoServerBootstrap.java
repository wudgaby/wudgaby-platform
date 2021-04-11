package com.wudgaby.platform.sso.server;

import com.wudgaby.platform.core.annotation.EnableOwnFrame;
import com.wudgaby.platform.webcore.support.GlobalExceptionAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @ClassName : SsoServerBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 17:44
 * @Desc :  #todo role, resource表未实现.
 */
@SpringBootApplication
@EnableOwnFrame
@ComponentScan(value = "com.wudgaby.platform",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GlobalExceptionAdvice.class}))
public class SsoServerBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SsoServerBootstrap.class, args);
    }
}
