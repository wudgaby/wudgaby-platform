package com.wudgaby.starter.mybatisplus.helper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/24 0024 10:30
 * @desc :
 */
@Data
@ConfigurationProperties(prefix = "mybatis-helper")
public class MybatisHelperProp {
    private boolean demoEnv;
    private List<String> excludeUrl;
}
