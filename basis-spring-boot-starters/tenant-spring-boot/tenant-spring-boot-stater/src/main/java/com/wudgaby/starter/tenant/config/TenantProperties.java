package com.wudgaby.starter.tenant.config;

import com.wudgaby.starter.tenant.TenantModeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 租户 配置属性
 *
 * @author Lion Li
 */
@Data
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    /**
     * 是否启用
     */
    private boolean enable = true;

    /**
     * 多租户模式. 过滤器/拦截器.
     * 与认证执行顺序有关.
     */
    private TenantModeEnum type = TenantModeEnum.FILTER;

    /**
     * 排除表
     */
    private List<String> ignoreTables;

    /**
     * 排除url
     */
    private List<String> ignoreUrls;

}
