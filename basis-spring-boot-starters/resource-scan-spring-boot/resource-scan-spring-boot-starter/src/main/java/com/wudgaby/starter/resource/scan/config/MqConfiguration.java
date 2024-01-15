package com.wudgaby.starter.resource.scan.config;

import com.wudgaby.starter.resource.scan.mq.ResourceSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/2 14:37
 * @Desc :
 */
@ConditionalOnProperty(value = "resource.scan.register.type", havingValue = "MQ")
@EnableBinding({ResourceSource.class})
public class MqConfiguration {
}
