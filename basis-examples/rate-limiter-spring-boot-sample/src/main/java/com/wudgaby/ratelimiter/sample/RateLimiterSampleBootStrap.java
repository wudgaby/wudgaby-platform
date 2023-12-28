package com.wudgaby.ratelimiter.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/8 16:46
 * @Desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class RateLimiterSampleBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(RateLimiterSampleBootStrap.class, args);
    }
}
