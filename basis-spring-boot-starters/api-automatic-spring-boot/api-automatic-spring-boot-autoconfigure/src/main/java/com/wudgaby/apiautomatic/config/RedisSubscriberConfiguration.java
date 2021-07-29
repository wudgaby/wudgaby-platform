package com.wudgaby.apiautomatic.config;

import com.wudgaby.apiautomatic.consts.ApiSystemConst;
import com.wudgaby.apiautomatic.service.ISubscriberRedisListenerService;
import com.wudgaby.platform.core.util.AssertUtil;
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
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/24 15:21
 * @Desc : api消息订阅消费
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "api.register.type", havingValue = "REDIS_PUB_SUB", matchIfMissing = true)
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
        log.info("订阅API注册通道: {}", ApiSystemConst.REDIS_API_CHANNEL);
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new ChannelTopic(ApiSystemConst.REDIS_API_CHANNEL));
        return container;
    }

    /**
     * 绑定消息监听者和接收监听的方法
     * @param subscriberRedisService
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageListenerAdapter listenerAdapter(@Autowired(required = false) ISubscriberRedisListenerService subscriberRedisService){
        AssertUtil.notNull(subscriberRedisService, "请实现SubscriberRedisListenerService接口.");
        return new MessageListenerAdapter(subscriberRedisService,"receiveMessage");
    }
}
