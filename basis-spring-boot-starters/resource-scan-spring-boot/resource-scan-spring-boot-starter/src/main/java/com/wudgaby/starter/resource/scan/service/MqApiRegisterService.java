package com.wudgaby.starter.resource.scan.service;

import com.wudgaby.starter.resource.scan.mq.ResourceSource;
import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Collection;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/2 17:13
 * @Desc :   
 */
@Slf4j
public class MqApiRegisterService implements ApiRegisterService {
    @Autowired
    private ResourceSource resourceSource;

    @Override
    public void batchRegister(Collection<ResourceDefinition> resourceDefinitionList) {
        Message<Collection<ResourceDefinition>> message = MessageBuilder.withPayload(resourceDefinitionList).build();
        resourceSource.resourceChannel().send(message);
    }
}
