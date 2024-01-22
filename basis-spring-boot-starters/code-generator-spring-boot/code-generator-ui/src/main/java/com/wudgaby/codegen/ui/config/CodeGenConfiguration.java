package com.wudgaby.codegen.ui.config;

import com.wudgaby.codegen.ui.filter.CodeGenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/3 21:57
 * @Desc :
 */
@Configuration
public class CodeGenConfiguration {

    @Bean
    public CodeGenFilter codeGenFilter(CodeGenProperties codeGenProperties){
        return new CodeGenFilter(codeGenProperties.isEnabled());
    }

}
