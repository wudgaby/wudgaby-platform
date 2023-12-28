package com.wudgaby.swagger.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/9 15:17
 * @Desc :   
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class SwaggerSampleBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerSampleBootStrap.class, args);
    }
}
