package com.wudgaby.apiautomatic.config;

import com.wudgaby.apiautomatic.mq.ResourceSource;
import com.wudgaby.apiautomatic.service.MqApiRegisterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName : MqConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/2 14:37
 * @Desc :
 */
@ConditionalOnProperty(value = "api.register.type", havingValue = "MQ")
@EnableBinding({ResourceSource.class})
public class MqConfiguration {
    @Bean
    public MqApiRegisterService resourceService() {
        return new MqApiRegisterService();
    }
}
