package com.wudgaby.starter.enums.scanner.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * @author zhuCan
 * @description 码表扫描配置属性类
 * @since 2021-09-09 14:18
 **/
@ConfigurationProperties(prefix = "enum-scan")
@Data
public class EnumScanProperties {

    /**
     * 扫描的包路径
     */
    private List<String> scanPackages = Collections.singletonList("com.wudgaby");
}
