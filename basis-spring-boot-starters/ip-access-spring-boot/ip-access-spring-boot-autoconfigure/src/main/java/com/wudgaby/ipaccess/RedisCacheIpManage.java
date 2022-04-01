package com.wudgaby.ipaccess;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.wudgaby.ipaccess.enums.BwType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 12:09
 * @desc :
 */
@Slf4j
@AllArgsConstructor
public class RedisCacheIpManage implements IpManage{
    public static final String REDIS_IP_WHITE_KEY = "ip:access:white";
    public static final String REDIS_IP_BLACK_KEY = "ip:access:black";
    private final RedisTemplate redisTemplate;

    @Override
    public void add(String ip, BwType type) {
        if(type == null || StrUtil.isBlank(ip)) {
            return;
        }
        if(type == BwType.BLACK) {
            redisTemplate.opsForSet().add(REDIS_IP_BLACK_KEY, ip);
        } else {
            redisTemplate.opsForSet().add(REDIS_IP_WHITE_KEY, ip);
        }
    }

    @Override
    public void batchAdd(Collection<String> ipList, BwType type) {
        if(type == null || CollectionUtil.isEmpty(ipList)) {
            return;
        }
        if(type == BwType.BLACK) {
            ipList.forEach(ip -> {
                redisTemplate.opsForSet().add(REDIS_IP_BLACK_KEY, ip);
            });
        } else {
            ipList.forEach(ip -> {
                redisTemplate.opsForSet().add(REDIS_IP_WHITE_KEY, ip);
            });
        }
    }

    @Override
    public void del(String ip, BwType type) {
        if(type == null || StrUtil.isBlank(ip)) {
            return;
        }

        if(type == BwType.BLACK) {
            redisTemplate.opsForSet().remove(REDIS_IP_BLACK_KEY, ip);
        } else {
            redisTemplate.opsForSet().remove(REDIS_IP_WHITE_KEY, ip);
        }
    }

    @Override
    public void batchDel(Collection<String> ipList, BwType type) {
        if(type == null || CollectionUtil.isEmpty(ipList)) {
            return;
        }
        ipList.forEach(ip -> {
            del(ip, type);
        });
    }

    @Override
    public Collection<String> list(BwType type) {
        if(type == null) {
            return Sets.newHashSet();
        }

        if(type == BwType.BLACK) {
            return redisTemplate.opsForSet().members(REDIS_IP_BLACK_KEY);
        } else {
            return redisTemplate.opsForSet().members(REDIS_IP_WHITE_KEY);
        }
    }

    @Override
    public boolean containIp(String ip, BwType type) {
        if(type == null) {
            return false;
        }

        if(StrUtil.isBlank(ip)){
            return true;
        }

        if(CollectionUtil.isEmpty(this.list(type))){
            return true;
        }

        if(type == BwType.BLACK) {
            return redisTemplate.opsForSet().isMember(REDIS_IP_BLACK_KEY, ip);
        } else {
            return redisTemplate.opsForSet().isMember(REDIS_IP_WHITE_KEY, ip);
        }
    }
}
