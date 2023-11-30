package com.wudgaby.sample.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/29 0029 16:09
 * @desc :
 */
@SpringBootApplication
@ComponentScan("com.wudgaby")
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }
}
