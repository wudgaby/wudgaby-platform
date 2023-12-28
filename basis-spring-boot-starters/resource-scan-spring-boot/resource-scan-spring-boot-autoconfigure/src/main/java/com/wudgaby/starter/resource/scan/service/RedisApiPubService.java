package com.wudgaby.starter.resource.scan.service;

import com.wudgaby.redis.api.RedisSupport;
import com.wudgaby.starter.resource.scan.consts.ApiScanConst;
import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/2 17:13
 * @Desc :
 */
@Slf4j
public class RedisApiPubService implements ApiRegisterService{
    @Autowired
    private RedisSupport redisSupport;

    @Override
    public void batchRegister(Collection<ResourceDefinition> resourceDefinitionList) {
        if(CollectionUtils.isEmpty(resourceDefinitionList)){
            return;
        }
        redisSupport.convertAndSend(ApiScanConst.REDIS_API_CHANNEL, resourceDefinitionList);
    }
}
