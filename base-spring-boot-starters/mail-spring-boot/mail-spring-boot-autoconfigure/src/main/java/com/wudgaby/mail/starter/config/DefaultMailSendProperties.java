package com.wudgaby.mail.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : MailReceiveProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/4/12/012 15:33
 * @Desc :   TODO
 */
@Data
@Configuration
@ConfigurationProperties(DefaultMailSendProperties.PROP_PREFIX)
public class DefaultMailSendProperties {
    public static final String PROP_PREFIX = "mail.send";

    private boolean pretendSendHeader;

}
