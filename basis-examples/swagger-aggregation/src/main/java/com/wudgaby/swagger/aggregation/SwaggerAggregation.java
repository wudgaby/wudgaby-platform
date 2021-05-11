package com.wudgaby.swagger.aggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/5/11 19:22
 * @Desc :
 */

@EnableSwagger2WebMvc
@SpringBootApplication
public class SwaggerAggregation {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerAggregation.class, args);
    }
}