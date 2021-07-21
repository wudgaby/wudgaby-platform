package com.wudgaby.platform.permission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/11 15:55
 * @Desc :
 */
@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.wudgaby.platform"})
@MapperScan("com.wudgaby.platform.permission.mapper")
public class PermissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }
}
