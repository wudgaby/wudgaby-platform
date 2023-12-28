package com.wudgaby.platform.message.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/10 9:54
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jpush")
public class JPushProperties {
    private String appKey;
    private String appSecure;
}
