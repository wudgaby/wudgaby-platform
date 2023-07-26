package com.wudgaby.apiautomatic.service;

import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.apiautomatic.event.ApiEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

/**
 * @ClassName : EventApiRegisterService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/2 17:13
 * @Desc :   
 */
@Slf4j
public class EventApiRegisterService implements ApiRegisterService {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void batchRegister(Collection<ApiDTO> apiDTOList) {
        log.info("批量注册API -- {}个", apiDTOList.size());
        publisher.publishEvent(new ApiEvent(apiDTOList));
    }

    /**
     * 使用以下方式消费数据
     */
    /*@Async
    @Order
    @EventListener(ApiEvent.class)
    public void eventHandler(ApiEvent data) {
        log.info("参数：" + data.getApiDTOList());
    }*/
}
