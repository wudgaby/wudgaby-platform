package com.wudgaby.platform.message.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : AliPushProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 9:54
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ali-push")
public class AliPushProperties {
    private Long appKey;
    private String appSecure;
    private String apnsEnv;
}
