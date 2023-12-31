package com.wudgaby.starter.mybatisplus.helper.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.wudgaby.starter.mybatisplus.helper.mybatis.CustomDatabaseIdProvider;
import com.wudgaby.starter.mybatisplus.helper.mybatis.CustomMetaObjectHandler;
import com.wudgaby.starter.mybatisplus.helper.mybatis.DemoProfileSqlInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/2/14 0014 16:05
 * @desc :
 */
@Configuration
@EnableConfigurationProperties(MybatisHelperProp.class)
public class MyBatisConfig {
    @Resource
    private MybatisHelperProp mybatisHelperProp;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(-1L);
        // 分页合理化
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

    /**
     * 乐观锁
     */
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

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
    @ConditionalOnWebApplication
    public Interceptor demoProfileSqlInterceptor(){
        return new DemoProfileSqlInterceptor(mybatisHelperProp.getExcludeUrl());
    }
}
