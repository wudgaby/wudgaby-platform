package com.wudgaby.ratelimiter.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : RateLimiterSampleBootStrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/8 16:46
 * @Desc :   TODO
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class RateLimiterSampleBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(RateLimiterSampleBootStrap.class, args);
    }
}
