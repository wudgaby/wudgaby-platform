package com.wudgaby.sample.data.sensitive;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/11 0011 18:15
 * @desc :
 */
@SpringBootApplication(scanBasePackages = {"com.wudgaby.sample", "com.wudgaby.platform"})
@EnableKnife4j
public class DataSensitiveApp {
    public static void main(String[] args) {
        SpringApplication.run(DataSensitiveApp.class, args);
    }
}
