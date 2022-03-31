package com.wudgaby.redis.starter.config;

import com.wudgaby.redis.starter.consts.RedisConst;
import com.wudgaby.redis.starter.enums.RedisConvertType;
import com.wudgaby.redis.starter.support.BeanHelper;
import com.wudgaby.redis.starter.support.FastJson2JsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/29 0029 15:02
 * @desc :
 */
@Slf4j
@Configuration
@ConditionalOnClass(value = {RedisAutoConfiguration.class})
public class RedisAloneConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedisProp redisProp = applicationContext.getBean(RedisProp.class);
        if(MapUtils.isEmpty(redisProp.getAlones())){
            return;
        }

        redisProp.getAlones().forEach((k,v) -> {
            //registerBean((ConfigurableApplicationContext)applicationContext, REDIS_BEAN_NAME_PREFIX + k, createRedisTemplate(v));
            log.info("动态注册Bean: {}", RedisConst.REDIS_BEAN_NAME_PREFIX + k);
            BeanHelper.registerSingletonBean((ConfigurableApplicationContext)applicationContext, RedisConst.REDIS_BEAN_NAME_PREFIX + k, createRedisTemplate(v, redisProp.getValueConvert()));

            log.info("动态注册Bean: {}", RedisConst.STRING_REDIS_BEAN_NAME_PREFIX + k);
            BeanHelper.registerSingletonBean((ConfigurableApplicationContext)applicationContext, RedisConst.STRING_REDIS_BEAN_NAME_PREFIX + k, createStringRedisTemplate(v));
        });
    }

    private RedisTemplate createRedisTemplate(RedisProperties redisProperties, RedisConvertType type) {
        // 创建一个模板类
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 将刚才的redis连接工厂设置到模板类中
        template.setConnectionFactory(getRedisConnectionFactory(redisProperties));

        // 设置key的序列化器
        template.setKeySerializer(RedisSerializer.string());
        // 设置hash key的序列化器
        template.setHashKeySerializer(RedisSerializer.string());

        if (type == RedisConvertType.JACKSON) {
            template.setValueSerializer(RedisSerializer.json());
            template.setHashValueSerializer(RedisSerializer.json());
        }else if (type == RedisConvertType.FASTJSON) {
            FastJson2JsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
            template.setValueSerializer(fastJsonRedisSerializer);
            template.setHashValueSerializer(fastJsonRedisSerializer);
        } else if (type == RedisConvertType.STRING) {
            template.setValueSerializer(RedisSerializer.string());
            template.setHashValueSerializer(RedisSerializer.string());
        }
        template.afterPropertiesSet();
        return template;
    }

    private RedisTemplate createStringRedisTemplate(RedisProperties redisProperties) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(getRedisConnectionFactory(redisProperties));
        return template;
    }

    private RedisConnectionFactory getRedisConnectionFactory(RedisProperties redisProperties) {
        // 基本配置
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setDatabase(redisProperties.getDatabase());
        if (Strings.isNotBlank(redisProperties.getPassword())) {
            configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }

        // 连接池配置
        GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        genericObjectPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());

        // lettuce pool
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        builder.poolConfig(genericObjectPoolConfig);
        builder.commandTimeout(redisProperties.getTimeout());

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(configuration, builder.build());
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }
}
