package com.wudgaby.mail.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/3/31 14:46
 * @Desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class MailDemoBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(MailDemoBootstrap.class, args);
    }
}
