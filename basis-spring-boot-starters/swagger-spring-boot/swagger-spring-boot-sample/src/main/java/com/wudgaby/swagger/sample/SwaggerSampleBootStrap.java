package com.wudgaby.swagger.sample;

import com.wudgaby.platform.core.annotation.EnableOwnFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : SwaggerSampleBootStrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 15:17
 * @Desc :   TODO
 */
@EnableOwnFrame(scanBasePackageClasses = {SwaggerSampleBootStrap.class})
@SpringBootApplication
public class SwaggerSampleBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerSampleBootStrap.class, args);
    }
}