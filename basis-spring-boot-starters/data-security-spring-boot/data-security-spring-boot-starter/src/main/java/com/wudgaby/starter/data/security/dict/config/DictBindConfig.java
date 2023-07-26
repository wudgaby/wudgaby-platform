package com.wudgaby.starter.data.security.dict.config;


import com.wudgaby.starter.data.security.dict.IDictSource;
import com.wudgaby.starter.data.security.dict.handler.BeanDictBindHandler;
import com.wudgaby.starter.data.security.dict.handler.DictHandlerFactory;
import com.wudgaby.starter.data.security.dict.handler.StringDictBindHandler;
import com.wudgaby.starter.data.security.dict.interceptor.DictBindReadInterceptor;
import com.wudgaby.starter.data.security.enums.HandlerType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author wudgaby
 */
@Configuration
@ConditionalOnProperty(value = "crypto.data.db-dict-bind", havingValue = "true", matchIfMissing = false)
public class DictBindConfig {
    @Resource
    private IDictSource dictSource;

    @PostConstruct
    public void dictHandlerFactory(){
        if(dictSource == null){
            throw new IllegalArgumentException("请实现IDictSource");
        }
        DictHandlerFactory.put(HandlerType.STRING, new StringDictBindHandler(dictSource));
        DictHandlerFactory.put(HandlerType.BEAN, new BeanDictBindHandler());
    }

    @Bean
    @ConditionalOnMissingBean
    public DictBindReadInterceptor dictBindReadInterceptor(){
        return new DictBindReadInterceptor();
    }
}
