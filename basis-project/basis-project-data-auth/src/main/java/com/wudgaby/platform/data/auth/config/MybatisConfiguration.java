package com.wudgaby.platform.data.auth.config;

import com.wudgaby.platform.data.auth.interceptor.MybatisDataAuthInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @ClassName : MybatisConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/7/8 16:47
 * @Desc :
 */
@Configuration
public class MybatisConfiguration {

    @Bean
    public Interceptor mybatisDataAuthInterceptor(){
        Interceptor interceptor = new MybatisDataAuthInterceptor();
        Properties properties = new Properties();
        interceptor.setProperties(properties);
        return interceptor;
    }
}
