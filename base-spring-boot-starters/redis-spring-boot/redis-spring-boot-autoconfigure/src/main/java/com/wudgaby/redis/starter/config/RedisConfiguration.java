package com.wudgaby.redis.starter.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.wudgaby.redis.api.RedisSupport;
import com.wudgaby.redis.starter.enums.RedisConvertType;
import com.wudgaby.redis.starter.support.FastJson2JsonRedisSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : SecureRegistry
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/6/289:26
 * @Desc :  redis 配置
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@ConditionalOnClass(value = {RedisAutoConfiguration.class})
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {
    @Bean
    @ConditionalOnMissingBean(RedisSupport.class)
    public RedisSupport redisSupport(RedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate) {
        return new RedisSupport(redisTemplate, stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory factory, RedisProperties redisProperties) {
        // 创建一个模板类
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 将刚才的redis连接工厂设置到模板类中
        template.setConnectionFactory(factory);

        if (redisProperties.getKeyConvert() == RedisConvertType.STRING) {
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            // 设置key的序列化器
            template.setKeySerializer(stringRedisSerializer);
            // hash的key也采用String的序列化方式
            template.setHashKeySerializer(stringRedisSerializer);
        }

        if (redisProperties.getValueConvert() == RedisConvertType.JACKSON) {
            // 设置value的序列化器
            //使用Jackson 2，将对象序列化为JSON
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
            //GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
            //json转对象类，不设置默认的会将json转成hashmap
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            //om.activateDefaultTyping(BasicPolymorphicTypeValidator.builder().build(), ObjectMapper.DefaultTyping.NON_FINAL);
            om.activateDefaultTyping(om.getPolymorphicTypeValidator(),ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(om);

            template.setValueSerializer(jackson2JsonRedisSerializer);
            template.setHashValueSerializer(jackson2JsonRedisSerializer);
        }else if (redisProperties.getValueConvert() == RedisConvertType.FASTJSON) {
            FastJson2JsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
            template.setValueSerializer(fastJsonRedisSerializer);
            template.setHashValueSerializer(fastJsonRedisSerializer);
        } else if (redisProperties.getValueConvert() == RedisConvertType.STRING) {
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            template.setValueSerializer(stringRedisSerializer);
            template.setHashValueSerializer(stringRedisSerializer);
        }
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (Object target, Method method, Object... params) ->{
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append("#");
            sb.append(method.getName());
            sb.append("#");
            for (Object obj : params) {
                sb.append(obj.toString());
                sb.append(",");
            }
            return sb.toString();
        };
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                this.getRedisCacheConfigurationWithTtl(600), // 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationMap() // 指定 key 策略
        );
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put("e30", this.getRedisCacheConfigurationWithTtl(3000));
        redisCacheConfigurationMap.put("e90", this.getRedisCacheConfigurationWithTtl(9000));
        redisCacheConfigurationMap.put("e180", this.getRedisCacheConfigurationWithTtl(18000));
        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }
}
