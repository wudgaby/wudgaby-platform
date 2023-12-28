package com.wudgaby.starter.resource.scan.config;

import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.starter.resource.scan.consts.ApiScanConst;
import com.wudgaby.starter.resource.scan.service.SubscriberRedisListenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/7/24 15:21
 * @Desc : api消息订阅消费
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "resource.scan.register.type", havingValue = "REDIS_PUB_SUB")
public class RedisSubscriberConfiguration {
    /**
     * 创建连接工厂
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter){
        log.info("订阅API注册通道: {}", ApiScanConst.REDIS_API_CHANNEL);
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new ChannelTopic(ApiScanConst.REDIS_API_CHANNEL));
        return container;
    }

    /**
     * 绑定消息监听者和接收监听的方法
     * @param subscriberRedisService
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageListenerAdapter listenerAdapter(@Autowired(required = false) SubscriberRedisListenerService subscriberRedisService){
        AssertUtil.notNull(subscriberRedisService, "请实现SubscriberRedisListenerService接口.");
        return new MessageListenerAdapter(subscriberRedisService,"receiveMessage");
    }
}
