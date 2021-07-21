package com.wudgaby.flowable.module.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : FlowableModuleConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/3 11:04
 * @Desc :   
 */
@Configuration
@ComponentScan(basePackages = {"com.wudgaby.flowable.module.web"})
public class FlowableModuleConfiguration {
}
