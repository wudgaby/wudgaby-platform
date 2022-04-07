package com.wudgaby.ip2region;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 15:13
 * @desc :
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(Ip2regionProperties.class)
public class Ip2regionAutoConfiguration {
    @Bean
    public Ip2RegionHelper ip2RegionHelper(ResourceLoader resourceLoader, Ip2regionProperties properties){
        return new Ip2RegionHelper(resourceLoader, properties);
    }
}
