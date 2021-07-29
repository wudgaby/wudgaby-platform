package com.wudgaby.apiautomatic.service;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/24 15:33
 * @Desc :
 */
public interface ISubscriberRedisListenerService {
    /**
     * 消费消息
     * @param message
     */
    void receiveMessage(String message);
}
