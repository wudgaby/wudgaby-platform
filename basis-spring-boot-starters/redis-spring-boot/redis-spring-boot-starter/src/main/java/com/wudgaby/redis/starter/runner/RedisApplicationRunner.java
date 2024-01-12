package com.wudgaby.redis.starter.runner;

import com.wudgaby.redis.api.RedisSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/12 0012 11:14
 * @desc :
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
//@Component
public class RedisApplicationRunner implements ApplicationRunner {
    @Resource
    private RedisSupport redisSupport;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean isConnected = redisSupport.isConnected();
        if(!isConnected){
            throw new RuntimeException("redis无法连接.");
        }
    }
}
