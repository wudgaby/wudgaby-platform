package com.wudgaby.swagger.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : SwaggerProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 15:02
 * @Desc :   TODO
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private boolean enabled = true;
    private boolean showDefaultGroup = true;
}
