package com.wudgaby.apiautomatic.service;

import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.apiautomatic.mq.ResourceSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import java.util.List;

/**
 * @ClassName : ResourceService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/2 17:13
 * @Desc :   
 */
@Slf4j
public class MqApiRegisterService implements ApiRegisterService {
    @Autowired
    private ResourceSource resourceSource;

    @Override
    public void register(ApiDTO apiDTO) {
        Message message = MessageBuilder.withPayload(apiDTO).build();
        resourceSource.resourceChannel().send(message);
        log.info("注册资源: {}", apiDTO);
    }

    @Override
    public void batchRegister(List<ApiDTO> apiDTOList) {
        Message message = MessageBuilder.withPayload(apiDTOList).build();
        resourceSource.resourceChannel().send(message);
    }
}
