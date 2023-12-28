package com.wudgaby.platform.twofactorauth.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/12/9 17:06
 * @Desc :   https://gitee.com/rstyro/spring-boot/tree/master/SpringBoot-Google-Check
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class GoogleAuthBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(GoogleAuthBootStrap.class, args);
    }
}
