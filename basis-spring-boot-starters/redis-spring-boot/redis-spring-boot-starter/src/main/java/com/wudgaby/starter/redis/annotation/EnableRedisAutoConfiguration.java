package com.wudgaby.starter.redis.annotation;

import com.wudgaby.starter.redis.config.RedisConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/8/16 10:32
 * @Desc :
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisConfiguration.class)
public @interface EnableRedisAutoConfiguration {
}
