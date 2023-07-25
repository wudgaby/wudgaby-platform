package com.wudgaby.apiautomatic.service;

import com.wudgaby.apiautomatic.consts.ApiSystemConst;
import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.redis.api.RedisSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * @ClassName : ResourceService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/2 17:13
 * @Desc :
 */
@Slf4j
public class RedisApiPubService implements ApiRegisterService{
    @Autowired
    private RedisSupport redisSupport;

    @Override
    public void batchRegister(Collection<ApiDTO> apiDTOList) {
        if(CollectionUtils.isEmpty(apiDTOList)){
            return;
        }
        log.info("批量发布注册API -- {}个", apiDTOList.size());
        redisSupport.convertAndSend(ApiSystemConst.REDIS_API_CHANNEL, apiDTOList);
    }
}
