package com.wudgaby.starter.resource.scan.config;

import com.wudgaby.starter.resource.scan.enums.RegisterType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/5/2 13:49
 * @Desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "resource.scan")
@Validated
public class ResourceScanProperties {
    private boolean enabled;
    @NotNull
    private RegisterType type = RegisterType.EVENT;
}
