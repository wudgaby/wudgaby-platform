package com.wudgaby.starter.crypto.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/4
 * @desc :
 */
@Data
@ConfigurationProperties(prefix = "api.crypto")
public class CryptoProperties {
    private String key;
    private Boolean simple;

    private AesProp custom;
    @Data
    static class AesProp {
        private String mode;
        private String padding;
        private String key;
        private String iv;
    }
}


