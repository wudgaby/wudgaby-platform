package com.wudgaby.platform.sso.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : SsoTokenSampleBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/24 15:51
 * @Desc :   TODO
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby.platform")
public class SsoTokenSampleBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SsoTokenSampleBootstrap.class, args);
    }
}
