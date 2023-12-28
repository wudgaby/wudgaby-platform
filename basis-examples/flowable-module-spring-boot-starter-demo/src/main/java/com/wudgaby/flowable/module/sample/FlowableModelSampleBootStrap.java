package com.wudgaby.flowable.module.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/9 17:37
 * @Desc :   
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableTransactionManagement
public class FlowableModelSampleBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(FlowableModelSampleBootStrap.class, args);
    }
}
