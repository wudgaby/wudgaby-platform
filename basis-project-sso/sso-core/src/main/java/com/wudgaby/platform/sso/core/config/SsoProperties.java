package com.wudgaby.platform.sso.core.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : SsoProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/24 9:55
 * @Desc :   TODO
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sso")
public class SsoProperties {
    /**
     * redis session 超时时间
     */
    private long sessionTimeout = TimeUnit.MINUTES.toMinutes(30);
    /**
     * sso server
     */
    private String server;
    /**
     * 客户端登出地址
     */
    private String logoutPath;
    /**
     * 排除地址
     */
    private List<String> excludedPaths;
    /**
     * 认证之后放行的地址
     */
    private List<AuthorizedExcludeResource> authorizedExcludePaths;
    /**
     * 系统标识
     */
    private String sysCode;

    @Data
    public static class AuthorizedExcludeResource{
        private String pattern;
        private String method;
    }
}