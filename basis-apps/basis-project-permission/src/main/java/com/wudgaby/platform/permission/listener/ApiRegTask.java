package com.wudgaby.platform.permission.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.entity.BaseApi;
import com.wudgaby.platform.permission.service.BaseApiService;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.redis.api.RedisSupport;
import com.wudgaby.starter.resource.scan.consts.ApiScanConst;
import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/25 0:02
 * @Desc :
 */
@Slf4j
@EnableScheduling
@AllArgsConstructor
@Component
public class ApiRegTask{
    private final BaseApiService baseApiService;
    private final BaseAuthorityService baseAuthorityService;
    private final RedisSupport redisSupport;

    @Scheduled(cron = "0 0/1 * * * *")
    public void receiveMessage() {
        log.info("定时获取API注册信息");
        Set<Object> apiSet = redisSupport.sGet(ApiScanConst.REDIS_API_SET);
        if(CollectionUtils.isEmpty(apiSet)){
            return;
        }

        Map<String, Set<String>> apiCodeMap = Maps.newHashMap();
        apiSet.forEach(api -> {
            ResourceDefinition resourceDefinition = JacksonUtil.deserialize(api.toString(), ResourceDefinition.class);
            BaseApi baseApi = new BaseApi();
            baseApi.setApiCode(resourceDefinition.getCode());
            baseApi.setApiName(resourceDefinition.getName());
            baseApi.setApiDesc(resourceDefinition.getDesc());
            baseApi.setRequestMethod(resourceDefinition.getMethod());
            baseApi.setServiceId(resourceDefinition.getServiceName());
            baseApi.setPath(resourceDefinition.getUri());
            baseApi.setStatus(AuthorityConst.ENABLED);
            baseApi.setIsAuth(BooleanUtils.isTrue(resourceDefinition.getAuth()) ? 1 : 0);
            baseApi.setClassName(resourceDefinition.getClassName());
            baseApi.setMethodName(resourceDefinition.getMethodName());

            BaseApi dbBaseApi = baseApiService.getOne(Wrappers.<BaseApi>lambdaQuery().eq(BaseApi::getApiCode,baseApi.getApiCode()));
            if (dbBaseApi == null) {
                baseApi.setIsOpen(0);
                baseApi.setIsPersist(1);
                baseApiService.addApi(baseApi);
            } else {
                baseApi.setApiId(dbBaseApi.getApiId());
                baseApiService.updateApi(baseApi);
            }
            redisSupport.setRemove(ApiScanConst.REDIS_API_SET, api);
            Set<String> apiCodeSet = apiCodeMap.computeIfAbsent(resourceDefinition.getServiceName(), k -> Sets.newHashSet());
            apiCodeSet.add(resourceDefinition.getCode());
        });

        // 清理无效权限数据
        apiCodeMap.forEach(baseAuthorityService::clearInvalidApi);
    }
}
