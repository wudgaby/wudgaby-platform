package com.wudgaby.apiRegister.sample;

import com.wudgaby.apiautomatic.annotation.EnableApiRegisterAutoConfiguration;
import com.wudgaby.platform.core.annotation.EnableOwnFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : ApiRegisterSampleBootStrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 15:17
 * @Desc :   TODO
 */
@EnableOwnFrame(scanBasePackageClasses = {ApiRegisterSampleBootStrap.class})
@SpringBootApplication
public class ApiRegisterSampleBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(ApiRegisterSampleBootStrap.class, args);
    }
}
