package com.wudgaby.platform.data.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : DataAuthBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/7/8 16:28
 * @Desc :   TODO
 */
@MapperScan("com.wudgaby.platform.data.auth.mapper")
@SpringBootApplication
public class DataAuthBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(DataAuthBootstrap.class, args);
    }
}
