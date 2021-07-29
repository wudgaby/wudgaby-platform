package com.wudgaby.platform.permission.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.houbb.heaven.util.secrect.Md5Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wudgaby.apiautomatic.consts.ApiSystemConst;
import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.apiautomatic.service.ISubscriberRedisListenerService;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.entity.BaseApi;
import com.wudgaby.platform.permission.service.BaseApiService;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.utils.FastJsonUtil;
import com.wudgaby.redis.api.RedisSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/25 0:02
 * @Desc :
 */
@Slf4j
@Service
@AllArgsConstructor
public class ApiSubRedisListenerService implements ISubscriberRedisListenerService {
    private final BaseApiService baseApiService;
    private final BaseAuthorityService baseAuthorityService;
    private final RedisSupport redisSupport;

    @Override
    public void receiveMessage(String message) {
        log.info("订阅API注册信息: {}", message);
        Set<ApiDTO> apiSet = Sets.newHashSet(FastJsonUtil.toList(message, ApiDTO.class));
        if(CollectionUtils.isEmpty(apiSet)) {
            return;
        }

        Map<String, Set<String>> apiCodeMap = Maps.newHashMap();
        apiSet.forEach(apiDTO -> {
            BaseApi baseApi = new BaseApi();
            baseApi.setApiCode(apiDTO.getCode());
            baseApi.setApiName(apiDTO.getName());
            baseApi.setApiDesc(apiDTO.getDesc());
            baseApi.setRequestMethod(apiDTO.getMethod());
            baseApi.setServiceId(apiDTO.getServiceName());
            baseApi.setPath(apiDTO.getUri());
            baseApi.setStatus(AuthorityConst.ENABLED);
            baseApi.setIsAuth(BooleanUtils.isTrue(apiDTO.getAuth()) ? 1 : 0);
            baseApi.setClassName(apiDTO.getClassName());
            baseApi.setMethodName(apiDTO.getMethodName());

            BaseApi dbBaseApi = baseApiService.getOne(Wrappers.<BaseApi>lambdaQuery().eq(BaseApi::getApiCode,baseApi.getApiCode()));
            if (dbBaseApi == null) {
                baseApi.setIsOpen(0);
                baseApi.setIsPersist(1);
                baseApiService.addApi(baseApi);
            } else {
                baseApi.setApiId(dbBaseApi.getApiId());
                baseApiService.updateApi(baseApi);
            }
            redisSupport.setRemove(ApiSystemConst.REDIS_API_SET, apiDTO);
            Set<String> apiCodeSet = apiCodeMap.computeIfAbsent(apiDTO.getServiceName(), k -> Sets.newHashSet());
            apiCodeSet.add(apiDTO.getCode());
        });

        apiCodeMap.forEach((k,v)->{
            // 清理无效权限数据
            baseAuthorityService.clearInvalidApi(k, v);
        });
    }
}
