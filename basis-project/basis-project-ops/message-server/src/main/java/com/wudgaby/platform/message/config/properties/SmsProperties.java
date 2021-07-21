package com.wudgaby.platform.message.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : SmsProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 9:54
 * @Desc :   
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
    private int appId;
    private String appKey;
    private String sign;
}
