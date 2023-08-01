package com.wudgaby.audit.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 17:02
 * @desc :
 */
@MapperScan("com.wudgaby.audit.sample.mapper")
@SpringBootApplication
public class DataAuditApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataAuditApplication.class, args);
    }
}
