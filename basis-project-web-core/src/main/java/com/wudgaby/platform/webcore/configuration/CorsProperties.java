package com.wudgaby.platform.webcore.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName : CorsProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/20 10:52
 * @Desc :   TODO
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private static final List<String> ALL = Arrays.asList("*");

    private String antPath = "/**";
    private boolean allowCredentials = true;
    private Duration maxAge = Duration.ofDays(1);

    private List<String> allowedOrigins = ALL;
    private List<String> allowedHeaders = ALL;
    private List<String> allowedMethods = ALL;
}
