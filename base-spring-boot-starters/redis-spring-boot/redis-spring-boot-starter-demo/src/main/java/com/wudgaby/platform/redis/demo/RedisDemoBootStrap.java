package com.wudgaby.platform.redis.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : RedisDemoBootStrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/7 12:36
 * @Desc :   TODO
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class RedisDemoBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(RedisDemoBootStrap.class, args);
    }
}
