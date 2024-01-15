package com.wudgaby.platform.permission.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.entity.BaseApi;
import com.wudgaby.platform.permission.service.BaseApiService;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.starter.redis.support.RedisSupport;
import com.wudgaby.starter.resource.scan.consts.ApiScanConst;
import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
import com.wudgaby.starter.resource.scan.service.SubscriberRedisListenerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/7/25 0:02
 * @Desc :
 */
@Slf4j
@Service
@AllArgsConstructor
public class ApiSubRedisListenerService implements SubscriberRedisListenerService {
    private final BaseApiService baseApiService;
    private final BaseAuthorityService baseAuthorityService;
    private final RedisSupport redisSupport;

    @Override
    public void receiveMessage(String message) {
        log.info("订阅API注册信息");
        Set<ResourceDefinition> apiSet = Sets.newHashSet(JacksonUtil.deserializeArray(message, ResourceDefinition.class));
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
            redisSupport.setRemove(ApiScanConst.REDIS_API_SET, apiDTO);
            Set<String> apiCodeSet = apiCodeMap.computeIfAbsent(apiDTO.getServiceName(), k -> Sets.newHashSet());
            apiCodeSet.add(apiDTO.getCode());
        });

        // 清理无效权限数据
        apiCodeMap.forEach(baseAuthorityService::clearInvalidApi);
    }
}
