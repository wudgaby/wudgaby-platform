package com.wudgaby.apiautomatic.config;

import com.wudgaby.apiautomatic.enums.RegisterType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/5/2 13:49
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "api.register")
public class AutoRegisterApiProperties {
    private boolean enabled;
    private RegisterType type;
}
