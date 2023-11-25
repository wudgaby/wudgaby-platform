package com.wudgaby.starter.resource.scan.service;

import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.redis.api.RedisSupport;
import com.wudgaby.starter.resource.scan.consts.ApiScanConst;
import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : ResourceService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/2 17:13
 * @Desc :
 */
@Slf4j
public class RedisApiRegisterService implements ApiRegisterService{
    @Autowired
    private RedisSupport redisSupport;

    @Override
    public void batchRegister(Collection<ResourceDefinition> resourceDefinitionList) {
        if(CollectionUtils.isEmpty(resourceDefinitionList)){
            return;
        }
        List<String> apiList = resourceDefinitionList.stream().map(JacksonUtil::serialize).collect(Collectors.toList());
        redisSupport.sSet(ApiScanConst.REDIS_API_SET, 3600, apiList.toArray(new String[apiList.size()]));
    }
}
