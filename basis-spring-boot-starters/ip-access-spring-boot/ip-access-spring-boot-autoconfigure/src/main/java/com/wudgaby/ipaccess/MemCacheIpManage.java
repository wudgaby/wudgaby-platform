package com.wudgaby.ipaccess;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Sets;
import com.wudgaby.ipaccess.enums.BwType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 12:09
 * @desc :
 */
@Slf4j
public class MemCacheIpManage implements IpManage{
    private final Cache<String, String> WHITE_CACHE = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    private final Cache<String, String> BLACK_CACHE = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    @Override
    public void add(String ip, BwType type) {
        if(type == null || StrUtil.isBlank(ip)) {
            return;
        }

        if(type == BwType.BLACK) {
            BLACK_CACHE.put(ip, ip);
        } else {
            WHITE_CACHE.put(ip, ip);
        }
    }

    @Override
    public void batchAdd(Collection<String> ipList, BwType type) {
        if(type == null || CollectionUtil.isEmpty(ipList)) {
            return;
        }
        if(type == BwType.BLACK) {
            ipList.forEach(ip -> {
                BLACK_CACHE.put(ip, ip);
            });
        } else {
            ipList.forEach(ip -> {
                WHITE_CACHE.put(ip, ip);
            });
        }
    }

    @Override
    public void del(String ip, BwType type) {
        if(type == null || StrUtil.isBlank(ip)) {
            return;
        }

        if(type == BwType.BLACK) {
            BLACK_CACHE.invalidate(ip);
        } else {
            WHITE_CACHE.invalidate(ip);
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
            return BLACK_CACHE.asMap().keySet();
        } else {
            return WHITE_CACHE.asMap().keySet();
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
            return BLACK_CACHE.getIfPresent(ip) != null;
        } else {
            return WHITE_CACHE.getIfPresent(ip) != null;
        }
    }
}
