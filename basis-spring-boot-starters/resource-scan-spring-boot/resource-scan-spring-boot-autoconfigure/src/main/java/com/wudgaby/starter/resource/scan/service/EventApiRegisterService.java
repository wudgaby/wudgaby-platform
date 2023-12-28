package com.wudgaby.starter.resource.scan.service;

import com.wudgaby.starter.resource.scan.event.ResourceScanEvent;
import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/2 17:13
 * @Desc :   
 */
@Slf4j
public class EventApiRegisterService implements ApiRegisterService {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void batchRegister(Collection<ResourceDefinition> resourceDefinitionList) {
        publisher.publishEvent(new ResourceScanEvent(resourceDefinitionList));
    }

    /**
     * 使用以下方式消费数据
     */
    /*@Async
    @Order
    @EventListener(ResourceScanEvent.class)
    public void eventHandler(ResourceScanEvent data) {
        log.info("参数：" + data.getApiDTOList());
    }*/
}
