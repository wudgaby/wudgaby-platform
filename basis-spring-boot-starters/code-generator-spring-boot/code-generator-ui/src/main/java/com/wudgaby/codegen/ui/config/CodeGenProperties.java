package com.wudgaby.codegen.ui.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/11 23:50
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "codegen")
public class CodeGenProperties {
    private String author = "WudGaby";
    private String outputPath = "D://codeGen";
    private String zipPath = "D://codeGen.rar";
    private boolean enabled = true;
}
