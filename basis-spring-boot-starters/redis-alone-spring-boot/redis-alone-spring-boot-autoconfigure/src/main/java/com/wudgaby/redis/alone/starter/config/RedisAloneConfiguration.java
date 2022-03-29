package com.wudgaby.redis.alone.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
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
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@ConditionalOnClass(value = {RedisAutoConfiguration.class})
@EnableConfigurationProperties(RedisAloneProperties.class)
public class RedisAloneConfiguration implements ApplicationContextAware {
    public static final String REDIS_BEAN_NAME_PREFIX = "redisTemplate_";
    public static final String STRING_REDIS_BEAN_NAME_PREFIX = "stringRedisTemplate_";

    /*@Bean
    public Map<String, RedisTemplate> redisTemplateMap(RedisAloneProperties redisAloneProperties) {
        if(MapUtils.isEmpty(redisAloneProperties.getAlones())){
            return null;
        }

        Map<String, RedisTemplate> redisTemplateMap = Maps.newHashMap();
        redisAloneProperties.getAlones().forEach((k,v) -> {
            redisTemplateMap.put(REDIS_BEAN_NAME_PREFIX + k, createRedisTemplate(v));
        });
        return redisTemplateMap;
    }*/

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedisAloneProperties redisAloneProperties = applicationContext.getBean(RedisAloneProperties.class);
        if(MapUtils.isEmpty(redisAloneProperties.getAlones())){
            return;
        }

        redisAloneProperties.getAlones().forEach((k,v) -> {
            //registerBean((ConfigurableApplicationContext)applicationContext, REDIS_BEAN_NAME_PREFIX + k, createRedisTemplate(v));
            log.info("动态注册Bean: {}", REDIS_BEAN_NAME_PREFIX + k);
            registerSingletonBean((ConfigurableApplicationContext)applicationContext, REDIS_BEAN_NAME_PREFIX + k, createRedisTemplate(v));

            log.info("动态注册Bean: {}", STRING_REDIS_BEAN_NAME_PREFIX + k);
            registerSingletonBean((ConfigurableApplicationContext)applicationContext, STRING_REDIS_BEAN_NAME_PREFIX + k, createStringRedisTemplate(v));
        });
    }

    private RedisTemplate createRedisTemplate(RedisProperties redisProperties) {
        // 创建一个模板类
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 将刚才的redis连接工厂设置到模板类中
        template.setConnectionFactory(getRedisConnectionFactory(redisProperties));

        // 设置key的序列化器
        template.setKeySerializer(RedisSerializer.string());
        // 设置hash key的序列化器
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置value的序列化器
        template.setValueSerializer(RedisSerializer.json());
        // 设置hash value的序列化器
        template.setHashValueSerializer(RedisSerializer.json());

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

    private <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz, Object... args) {
        if(applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + name);
            }
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }

    private Object registerSingletonBean(ConfigurableApplicationContext applicationContext, String beanName, Object singletonObject){
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        //动态注册bean.
        defaultListableBeanFactory.registerSingleton(beanName, singletonObject);
        //获取动态注册的bean.
        return applicationContext.getBean(beanName);
    }
}
