package com.wudgaby.platform.sso.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : SsoServerBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 17:44
 * @Desc :  #todo role, resource表未实现.
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby.platform")
/*@ComponentScan(value = "com.wudgaby.platform",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GlobalExceptionAdvice.class}))*/
public class SsoServerBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SsoServerBootstrap.class, args);
    }
}
