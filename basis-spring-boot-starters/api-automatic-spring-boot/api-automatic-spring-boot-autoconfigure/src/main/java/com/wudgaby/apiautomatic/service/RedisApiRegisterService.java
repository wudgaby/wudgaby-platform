package com.wudgaby.apiautomatic.service;

import com.wudgaby.apiautomatic.consts.ApiSystemConst;
import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.platform.utils.FastJsonUtil;
import com.wudgaby.redis.api.RedisSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
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
    public void register(ApiDTO apiDTO) {
        log.info("注册API -- {}", apiDTO);
        redisSupport.sSetAndTime(ApiSystemConst.REDIS_API_SET, 3600, FastJsonUtil.collectToString(apiDTO));
    }

    @Override
    public void batchRegister(Collection<ApiDTO> apiDTOList) {
        if(CollectionUtils.isEmpty(apiDTOList)){
            return;
        }
        log.info("批量注册API -- {}个", apiDTOList.size());
        List<String> apiList = apiDTOList.stream().map(apiDTO -> FastJsonUtil.collectToString(apiDTO)).collect(Collectors.toList());
        redisSupport.sSet(ApiSystemConst.REDIS_API_SET, 3600, apiList.toArray(new String[apiList.size()]));
    }
}
