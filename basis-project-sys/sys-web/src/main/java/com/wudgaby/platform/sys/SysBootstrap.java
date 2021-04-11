package com.wudgaby.platform.sys;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.wudgaby.platform.core.annotation.EnableOwnFrame;
import com.wudgaby.starter.logger.aop.EnableAccessLogger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName : SysBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/9/28 18:28
 * @Desc :   TODO
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby.platform")
@MapperScan(value = {"com.wudgaby.platform.sys.mapper"})
@EnableSwagger2
@EnableKnife4j
@EnableAccessLogger
@EnableOwnFrame
@EnableAsync
public class SysBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SysBootstrap.class, args);
    }
}
