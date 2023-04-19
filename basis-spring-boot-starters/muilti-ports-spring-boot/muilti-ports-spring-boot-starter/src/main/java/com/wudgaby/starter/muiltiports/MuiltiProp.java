package com.wudgaby.starter.muiltiports;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/19 0019 15:19
 * @desc :
 */
@Data
@ConfigurationProperties(prefix = "server")
public class MuiltiProp {
    /**
     * 提供给外部接口使用的端口
     */
    private String port;

    /**
     * 管理端口
     */
    private String managementPort;

    /**
     * 提供给内部接口使用的端口
     */
    private String trustedPort;

    /**
     * 内部接口 URL 路径前缀
     */
    private String trustedPathPrefix;
}
