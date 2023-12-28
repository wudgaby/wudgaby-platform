package com.wudgaby.starter.resource.scan.service;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/7/24 15:33
 * @Desc :  订阅
 */
public interface SubscriberRedisListenerService {
    /**
     * 消费消息
     * @param message
     */
    void receiveMessage(String message);
}
