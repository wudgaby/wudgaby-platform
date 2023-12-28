package com.wudgaby.dlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/3 18:36
 * @Desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class DLockBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(DLockBootstrap.class, args);
    }
}
