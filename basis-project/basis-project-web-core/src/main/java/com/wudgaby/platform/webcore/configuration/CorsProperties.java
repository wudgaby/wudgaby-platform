package com.wudgaby.platform.webcore.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/20 10:52
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String antPath = "/**";
    private boolean allowCredentials = true;
    private Duration maxAge = Duration.ofDays(1);

    private String allowedOrigins = "*";
    private String allowedHeaders = "*";
    private String allowedMethods = "*";
}
