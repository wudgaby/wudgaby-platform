package com.wudgaby.apiautomatic.config;

import com.wudgaby.apiautomatic.extend.WebServerStartedListener;
import com.wudgaby.apiautomatic.service.ApiRegisterService;
import com.wudgaby.apiautomatic.service.MqApiRegisterService;
import com.wudgaby.apiautomatic.service.RedisApiRegisterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/5/2 10:33
 * @Desc :  自动注册资源配置
 */
@Configuration
@ConditionalOnProperty(value = "api.register.enabled", havingValue = "true",matchIfMissing = true)
@Import({MqConfiguration.class})
public class AutoRegisterApiConfiguration {
    @Bean
    public WebServerStartedListener webServerStartedListener() {
        return new WebServerStartedListener();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "api.register.type", havingValue = "REDIS", matchIfMissing = true)
    public ApiRegisterService redisResourceService() {
        return new RedisApiRegisterService();
    }

    @ConditionalOnProperty(value = "api.register.type", havingValue = "MQ")
    public ApiRegisterService mqResourceService() {
        return new MqApiRegisterService();
    }
}
