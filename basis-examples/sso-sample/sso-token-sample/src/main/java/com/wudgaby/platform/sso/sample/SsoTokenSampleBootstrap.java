package com.wudgaby.platform.sso.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/24 15:51
 * @Desc :   
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby.platform")
public class SsoTokenSampleBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SsoTokenSampleBootstrap.class, args);
    }
}
