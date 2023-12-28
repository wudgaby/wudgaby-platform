package com.wudgaby.platform.sys;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.wudgaby.starter.logger.aop.EnableAccessLogger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/9/28 18:28
 * @Desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby.platform")
@MapperScan(value = {"com.wudgaby.platform.sys.mapper"})
@EnableSwagger2WebMvc
@EnableKnife4j
@EnableAccessLogger
@EnableAsync
public class SysBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SysBootstrap.class, args);
    }
}
