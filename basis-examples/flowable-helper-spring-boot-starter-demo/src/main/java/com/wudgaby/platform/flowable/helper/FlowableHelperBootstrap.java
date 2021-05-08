package com.wudgaby.platform.flowable.helper;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.wudgaby")
@MapperScan("com.wudgaby.platform.flowable.helper.mapper")
@EnableKnife4j
public class FlowableHelperBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(FlowableHelperBootstrap.class, args);
    }
}
