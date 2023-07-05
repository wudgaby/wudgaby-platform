package com.wudgaby.platform.permission.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wudgaby.apiautomatic.consts.ApiSystemConst;
import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.entity.BaseApi;
import com.wudgaby.platform.permission.service.BaseApiService;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.redis.api.RedisSupport;
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
        Set<Object> apiSet = redisSupport.sGet(ApiSystemConst.REDIS_API_SET);
        if(CollectionUtils.isEmpty(apiSet)){
            return;
        }

        Map<String, Set<String>> apiCodeMap = Maps.newHashMap();
        apiSet.forEach(api -> {
            ApiDTO apiDTO = JacksonUtil.deserialize(api.toString(), ApiDTO.class);
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
            redisSupport.setRemove(ApiSystemConst.REDIS_API_SET, api);
            Set<String> apiCodeSet = apiCodeMap.computeIfAbsent(apiDTO.getServiceName(), k -> Sets.newHashSet());
            apiCodeSet.add(apiDTO.getCode());
        });

        apiCodeMap.forEach((k,v)->{
            // 清理无效权限数据
            baseAuthorityService.clearInvalidApi(k, v);
        });
    }
}
