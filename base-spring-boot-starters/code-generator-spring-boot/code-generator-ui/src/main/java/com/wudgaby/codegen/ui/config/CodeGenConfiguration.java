package com.wudgaby.codegen.ui.config;

import com.wudgaby.codegen.ui.filter.CodeGenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @ClassName : CodeGenConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/3 21:57
 * @Desc :   TODO
 */
@Configuration
public class CodeGenConfiguration {
    @Autowired private Environment environment;
    @Autowired private CodeGenProperties codeGenProperties;

    @Bean
    public CodeGenFilter codeGenFilter(){
        return new CodeGenFilter(codeGenProperties.isEnabled());
    }

}
