package com.wudgaby.apiautomatic.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName : ResourceSink
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/2 18:16
 * @Desc :
 */
public interface ResourceSink {
    String INPUT = "resource-input";

    @Input(ResourceSink.INPUT)
    SubscribableChannel resourceChannel();
}
