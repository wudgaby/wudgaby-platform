package com.wudgaby.apiautomatic.service;

import com.wudgaby.apiautomatic.consts.ApiRegisterConst;
import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.redis.api.RedisSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        redisSupport.lSet(ApiRegisterConst.REDIS_API_LIST, apiDTO);
    }

    @Override
    public void batchRegister(List<ApiDTO> apiDTOList) {
        redisSupport.lSet(ApiRegisterConst.REDIS_API_LIST, apiDTOList);
    }
}
