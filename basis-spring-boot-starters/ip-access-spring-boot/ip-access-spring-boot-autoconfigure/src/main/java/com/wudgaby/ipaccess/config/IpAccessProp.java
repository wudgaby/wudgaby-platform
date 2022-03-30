package com.wudgaby.ipaccess.config;

import com.wudgaby.ipaccess.enums.StrategyType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 10:12
 * @desc :
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ip.access")
public class IpAccessProp {
    private boolean enabled;

    /**
     * 限制模式.
     */
    private StrategyType strategy = StrategyType.AUTHORITY_MIXTURE;

    /**
     * 白名单. 支持格式:
     * 指定IP: X.X.X.X
     * IP段: X.X.X.X-X.X.X.X
     * 掩码位: X.X.X.X/mask
     */
    private List<String> whiteList;

    /**
     * 黑名单. 支持格式:
     * 指定IP: X.X.X.X
     * IP段: X.X.X.X-X.X.X.X
     * 掩码位: X.X.X.X/mask
     */
    private List<String> blackList;
}
