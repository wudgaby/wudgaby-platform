package com.wudgaby.platform.webcore.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.google.common.collect.Lists;
import com.wudgaby.platform.webcore.mybatis.CustomDatabaseIdProvider;
import com.wudgaby.platform.webcore.mybatis.CustomMetaObjectHandler;
import com.wudgaby.platform.webcore.mybatis.DemoProfileSqlInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/2/14 0014 16:05
 * @desc :
 */
@Configuration
public class MyBatisConfig {

    /**
     * 数据库id选择器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DatabaseIdProvider customDatabaseIdProvider(){
        return new CustomDatabaseIdProvider();
    }

    /**
     * mybatis plus 字段填充
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler customMetaObjectHandler(){
        return new CustomMetaObjectHandler();
    }

    /**
     * 演示环境
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "demoEnv", havingValue = "true")
    @ConditionalOnMissingBean
    public Interceptor demoProfileSqlInterceptor(){
        return new DemoProfileSqlInterceptor(getNoneSecurityUrl());
    }

    /**
     * 获取不需要验证的url
     * @return
     */
    private List<String> getNoneSecurityUrl(){
        List<String> list = Lists.newArrayList();
        list.add("/ss/login");
        list.add("/ss/logout");
        return list;
    }
}
