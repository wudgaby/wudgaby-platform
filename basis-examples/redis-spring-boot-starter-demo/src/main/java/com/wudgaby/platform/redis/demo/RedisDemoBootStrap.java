package com.wudgaby.platform.redis.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/7 12:36
 * @Desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class RedisDemoBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(RedisDemoBootStrap.class, args);
    }
}
