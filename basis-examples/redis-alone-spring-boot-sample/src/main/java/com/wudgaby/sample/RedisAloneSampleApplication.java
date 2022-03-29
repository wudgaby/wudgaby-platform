package com.wudgaby.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/29 0029 16:36
 * @desc :
 */
@SpringBootApplication
public class RedisAloneSampleApplication implements ApplicationListener<ApplicationStartedEvent> {
    @Resource(name = "redisTemplate_write")
    @Lazy
    private RedisTemplate redisTemplateWrite;

    @Resource(name = "redisTemplate_read")
    @Lazy
    private RedisTemplate redisTemplateRead;

    public static void main(String[] args) {
        SpringApplication.run(RedisAloneSampleApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        //event.getApplicationContext().getBean("redisTemplateMap");
        event.getApplicationContext().getBean("stringRedisTemplate");

        System.out.println(redisTemplateWrite.delete("a"));
        redisTemplateWrite.opsForValue().set("a", "a");
        System.out.println(redisTemplateWrite.opsForValue().get("a"));

        System.out.println(redisTemplateRead.delete("b"));
        redisTemplateRead.opsForValue().set("b", "b");
        System.out.println(redisTemplateRead.opsForValue().get("b"));
    }
}
