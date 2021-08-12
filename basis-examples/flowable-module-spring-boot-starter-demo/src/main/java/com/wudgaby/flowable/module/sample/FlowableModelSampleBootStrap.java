package com.wudgaby.flowable.module.sample;

import com.wudgaby.platform.core.annotation.EnableOwnFrame;
import com.wudgaby.platform.webcore.configuration.FastJsonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @ClassName : FlowableModelSampleBootStrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 17:37
 * @Desc :   
 */
@EnableOwnFrame(scanBasePackageClasses = {FlowableModelSampleBootStrap.class})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = FastJsonAutoConfiguration.class))
public class FlowableModelSampleBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(FlowableModelSampleBootStrap.class, args);
    }
}
