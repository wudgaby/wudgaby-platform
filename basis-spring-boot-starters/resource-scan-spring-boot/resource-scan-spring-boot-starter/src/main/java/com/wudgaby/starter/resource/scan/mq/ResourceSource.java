package com.wudgaby.starter.resource.scan.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/7 18:14
 * @Desc :   
 */
public interface ResourceSource {
    String OUTPUT = "resource-output";

    @Output(ResourceSource.OUTPUT)
    MessageChannel resourceChannel();
}
