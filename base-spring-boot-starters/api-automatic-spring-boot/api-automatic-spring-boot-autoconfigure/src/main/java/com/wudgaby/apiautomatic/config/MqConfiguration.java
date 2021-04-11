package com.wudgaby.apiautomatic.config;

import com.wudgaby.apiautomatic.extend.WebServerStartedListener;
import com.wudgaby.apiautomatic.mq.ResourceSource;
import com.wudgaby.apiautomatic.service.ResourceService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName : MqConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/2 14:37
 * @Desc :   TODO
 */
@EnableBinding({ResourceSource.class})
public class MqConfiguration {
    @Bean
    public WebServerStartedListener webServerStartedListener() {
        return new WebServerStartedListener();
    }

    @Bean
    public ResourceService resourceService() {
        return new ResourceService();
    }
}
