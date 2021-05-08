package com.wudgaby.platform.message.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : AliProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/12/20 14:45
 * @Desc :   TODO
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ali")
public class AliProperties {
    private String accessKeyId;
    private String accessKeySecret;
}
