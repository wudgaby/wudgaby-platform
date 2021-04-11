package com.wudgaby.apiautomatic.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @ClassName : Resourcesource
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/7 18:14
 * @Desc :   TODO
 */
public interface ResourceSource {
    String OUTPUT = "resource-output";

    @Output(ResourceSource.OUTPUT)
    MessageChannel resourceChannel();
}
