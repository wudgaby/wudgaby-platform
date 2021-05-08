package com.wudgaby.redis.starter.annotation;

import com.wudgaby.redis.starter.config.RedisConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName : EnableRedisAutoConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/16 10:32
 * @Desc :   TODO
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisConfiguration.class)
public @interface EnableRedisAutoConfiguration {
}
