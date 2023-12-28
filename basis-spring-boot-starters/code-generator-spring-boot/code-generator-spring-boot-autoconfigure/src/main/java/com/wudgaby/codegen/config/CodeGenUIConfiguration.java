package com.wudgaby.codegen.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/3 11:04
 * @Desc :
 */
@Configuration
@ComponentScan(basePackages = {
        "com.wudgaby.codegen.ui.controller",
        "com.wudgaby.codegen.ui.service",
        "com.wudgaby.codegen.ui.config"
})
public class CodeGenUIConfiguration {
}
