package com.wudgaby.starter.resource.scan.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/2 18:16
 * @Desc :
 */
public interface ResourceSink {
    String INPUT = "resource-input";

    @Input(ResourceSink.INPUT)
    SubscribableChannel resourceChannel();
}
