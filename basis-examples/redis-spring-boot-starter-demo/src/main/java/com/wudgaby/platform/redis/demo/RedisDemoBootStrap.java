package com.wudgaby.platform.redis.demo;

import com.wudgaby.redis.starter.consts.RedisConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/7 12:36
 * @Desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby")
public class RedisDemoBootStrap  implements ApplicationListener<ApplicationStartedEvent> {
    @Resource(name = RedisConst.REDIS_BEAN_NAME_PREFIX + "write")
    @Lazy
    private RedisTemplate redisTemplateWrite;

    @Resource(name = RedisConst.REDIS_BEAN_NAME_PREFIX + "read")
    @Lazy
    private RedisTemplate redisTemplateRead;
    public static void main(String[] args) {
        SpringApplication.run(RedisDemoBootStrap.class, args);
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
