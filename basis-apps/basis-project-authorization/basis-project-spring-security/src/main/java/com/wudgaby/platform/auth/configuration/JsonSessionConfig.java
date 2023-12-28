package com.wudgaby.platform.auth.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/23 15:22
 * @Desc :
 */
@Configuration
public class JsonSessionConfig implements BeanClassLoaderAware {

    private ClassLoader loader;

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        //很多类无法反序列化
        //return new GenericJackson2JsonRedisSerializer(objectMapper());
        //无法反序列化DefaultSavedRequest
        //return new GenericFastJsonRedisSerializer();

        return new Jackson2JsonRedisSerializer<>(Object.class);
    }

    /**
     * Customized {@link ObjectMapper} to add mix-in for class that doesn't have default
     * constructors
     *
     * @return the {@link ObjectMapper} to use
     */
    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
        return mapper;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }

    /**
     * redis.clients.jedis.exceptions.JedisDataException: ERR unknown command 'CONFIG'
     * 原因是spring-session调用了CONFIG命令来配置Keyspace notifications功能。而线上环境的redis基于权限的考虑，禁止应用服务执行CONFIG命令，这样就导致了应用在初始化时抛出此异常。
     *
     * 解决方案：
     * 1、打开redis的Keyspace notifications功能，在redis.conf配置文件里添加下面的配置项：
     * notify-keyspace-events Ex
     *
     * 关闭Spring-session使用CONFIG命令做操作
     * @return
     */
    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
