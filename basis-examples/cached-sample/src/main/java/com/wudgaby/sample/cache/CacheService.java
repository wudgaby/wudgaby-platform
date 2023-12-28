package com.wudgaby.sample.cache;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2022/1/20 0020 12:02
 * @Desc :
 */
@Slf4j
@Service
public class CacheService {
    public static Map<String, Integer> map = Maps.newHashMap();
    static {
        map.put("a", 18);
        map.put("b", 20);
        map.put("c", 12);
        map.put("d", 50);
        map.put("e", 30);
    }

    @Cacheable(value = {"user_age","age"}, key = "#name")
    public Integer getAge(String name){
        log.info("from memory");
        return map.get(name);
    }

    @CachePut(value = "user_age", key = "#name")
    public Integer update(String name, int age){
        map.put(name, age);
        return age;
    }

    @CacheEvict(value = {"user_age", "age"}, key = "#name")
    public void del(String name){
        map.remove(name);
    }

    @Cacheable(value = "all#3600")
    public Collection<Integer> getAll(){
        log.info("from memory");
        return map.values();
    }

}
