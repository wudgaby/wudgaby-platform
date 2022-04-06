package com.wudgaby.ip2region;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 15:12
 * @desc :
 */
@Data
@ConfigurationProperties(Ip2regionProperties.PREFIX)
public class Ip2regionProperties {
    public static final String PREFIX = "ip2region";

    /**
     * ip2region.db 文件路径
     */
    private String dbFileLocation = "classpath:ip2region/ip2region.db";
}
