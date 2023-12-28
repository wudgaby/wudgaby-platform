package com.wudgaby.mail.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/4/12/012 15:33
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(DefaultMailSendProperties.PROP_PREFIX)
public class DefaultMailSendProperties {
    public static final String PROP_PREFIX = "mail.send";

    private boolean pretendSendHeader;

}
