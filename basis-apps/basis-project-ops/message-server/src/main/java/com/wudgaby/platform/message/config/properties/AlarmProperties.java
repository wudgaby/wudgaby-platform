package com.wudgaby.platform.message.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/10 17:01
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alarm")
public class AlarmProperties {
    private boolean sendMail;
    private String[] sendTo;
}
