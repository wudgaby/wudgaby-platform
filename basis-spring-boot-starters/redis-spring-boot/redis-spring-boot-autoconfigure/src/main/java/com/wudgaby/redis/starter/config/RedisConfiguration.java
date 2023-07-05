package com.wudgaby.redis.starter.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wudgaby.redis.api.RedisSupport;
import com.wudgaby.redis.starter.enums.RedisConvertType;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

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
@EnableConfigurationProperties(RedisProp.class)
@EnableCaching
public class RedisConfiguration {
    @Bean
    @ConditionalOnMissingBean(RedisSupport.class)
    public RedisSupport redisSupport(RedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate) {
        return new RedisSupport(redisTemplate, stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory factory, RedisProp redisProperties) {
        // 创建一个模板类
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 将刚才的redis连接工厂设置到模板类中
        template.setConnectionFactory(factory);

        if (redisProperties.getKeyConvert() == RedisConvertType.STRING) {
            // 设置key的序列化器
            template.setKeySerializer(RedisSerializer.string());
            // hash的key也采用String的序列化方式
            template.setHashKeySerializer(RedisSerializer.string());
        }

        if (redisProperties.getValueConvert() == RedisConvertType.JACKSON) {
            // 设置value的序列化器
            //使用Jackson 2，将对象序列化为JSON
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();

            template.setValueSerializer(jackson2JsonRedisSerializer);
            template.setHashValueSerializer(jackson2JsonRedisSerializer);
        } else {
            template.setValueSerializer(RedisSerializer.string());
            template.setHashValueSerializer(RedisSerializer.string());
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
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisProp redisProperties) {
        RedisCacheConfiguration redisCacheConfiguration;
        if(redisProperties.getValueConvert() == RedisConvertType.JACKSON){
            redisCacheConfiguration = jacksonRedisCacheConfigurationWithTtl(600);
        }else{
            redisCacheConfiguration = stringRedisCacheConfigurationWithTtl(600);
        }
        CustomRedisCacheManager redisCacheManager = new CustomRedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), redisCacheConfiguration);
        return redisCacheManager;
    }

    private RedisCacheConfiguration jacksonRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }

    private RedisCacheConfiguration stringRedisCacheConfigurationWithTtl(Integer seconds) {
        StringRedisSerializer redisSerializer = new StringRedisSerializer();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }

    private Jackson2JsonRedisSerializer jackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        /**
         * 如果是作为api返回结果，不需要反序列化时：不设置activateDefaultTyping或者使用EXISTING_PROPERTY。
         * 如果作为缓存等，需要反序列化时：一般使用 WRAPPER_ARRAY、WRAPPER_OBJECT、PROPERTY中的一种。如果不指定则默认使用的是WRAPPER_ARRAY。
         */
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        return jackson2JsonRedisSerializer;
    }
}
