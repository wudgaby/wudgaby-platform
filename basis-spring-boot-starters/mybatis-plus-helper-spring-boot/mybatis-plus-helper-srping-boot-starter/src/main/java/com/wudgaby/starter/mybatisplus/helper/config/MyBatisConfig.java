package com.wudgaby.starter.mybatisplus.helper.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
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
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    /**
     * 乐观锁
     * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /*@Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }*/

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
     * id生成器
     * @return
     */
    /*@Bean
    @ConditionalOnMissingBean
    public IdentifierGenerator iKeyGenerator() {
        return new CustomerIdGenerator();
    }*/

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
