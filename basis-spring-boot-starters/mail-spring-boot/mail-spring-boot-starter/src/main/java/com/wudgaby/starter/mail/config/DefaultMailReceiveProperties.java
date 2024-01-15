package com.wudgaby.starter.mail.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/4/12/012 15:33
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(DefaultMailReceiveProperties.PROP_PREFIX)
public class DefaultMailReceiveProperties {
    public static final String PROP_PREFIX = "mail.receive";

    @Value("#{'${mail.receive.folder-list}'.split(',')}")
    private List<String> folderList;
    private String accessorySavePath;
    private int defaultReceiveNum;
    private List<MailDomainProperties> supportMailList;

    @Data
    public static class MailDomainProperties{
        private List<String> domains;
        private Properties properties;
    }
}
