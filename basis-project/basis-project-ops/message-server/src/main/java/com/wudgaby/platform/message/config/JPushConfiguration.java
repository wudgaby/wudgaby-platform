package com.wudgaby.platform.message.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.wudgaby.platform.message.config.properties.JPushProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : JPushClient
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/18 10:53
 * @Desc :   
 */
@ConditionalOnClass(JPushClient.class)
@Configuration
public class JPushConfiguration {
    @Bean("jPushClient")
    public JPushClient jPushClient(JPushProperties jPushProperties){
        return new JPushClient(jPushProperties.getAppSecure(), jPushProperties.getAppKey(), null, ClientConfig.getInstance());
    }
}
