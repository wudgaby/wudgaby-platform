package com.wudgaby.dlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : DLockBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/3 18:36
 * @Desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class DLockBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(DLockBootstrap.class, args);
    }
}
