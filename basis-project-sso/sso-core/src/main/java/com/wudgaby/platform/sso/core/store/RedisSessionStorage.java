package com.wudgaby.platform.sso.core.store;

import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;
import com.wudgaby.redis.api.RedisSupport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @ClassName : RedisSessionStorage
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/4 10:13
 * @Desc :   TODO
 */
@Component
@AllArgsConstructor
public class RedisSessionStorage {
    private final RedisSupport redisSupport;
    private final SsoProperties ssoProperties;

    private final String REDIS_KEY_PREFIX = "sso:sessionid:";

    public void put(String storeKey, SsoUserVo ssoUserVo){
        redisSupport.set(REDIS_KEY_PREFIX + storeKey, ssoUserVo, ssoProperties.getSessionTimeout() * 60);
    }

    public SsoUserVo get(String storeKey){
        Object obj = redisSupport.get(REDIS_KEY_PREFIX + storeKey);
        return obj == null ? null : (SsoUserVo) obj;
    }

    public void remove(String storeKey){
        redisSupport.del(REDIS_KEY_PREFIX + storeKey);
    }
}
