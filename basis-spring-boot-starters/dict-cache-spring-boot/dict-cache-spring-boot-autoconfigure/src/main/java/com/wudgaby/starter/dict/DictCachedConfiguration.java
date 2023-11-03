package com.wudgaby.starter.dict;

import com.wudgaby.starter.dict.api.DictCachedApi;
import com.wudgaby.starter.dict.load.DefaultDictCacheImpl;
import com.wudgaby.starter.dict.load.DictCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 10:36
 * @desc :
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(name = {"org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"})
//@DependsOn({"flyway", "flywayInitializer"})
public class DictCachedConfiguration {
    @Bean
    @ConditionalOnMissingBean
    //@ConditionalOnBean(DictCachedApi.class)
    public DictCache dictLoader(@Autowired(required = false) DictCachedApi dictCachedApi){
        if(dictCachedApi == null){
            throw new RuntimeException("请实现DictCachedApi接口,并注册Bean");
        }
        return new DefaultDictCacheImpl(dictCachedApi);
    }
}
