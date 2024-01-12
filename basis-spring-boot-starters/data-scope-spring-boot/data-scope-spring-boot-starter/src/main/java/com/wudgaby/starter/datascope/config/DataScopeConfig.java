package com.wudgaby.starter.datascope.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.wudgaby.starter.datascope.DataScopeService;
import com.wudgaby.starter.datascope.interceptor.PlusDataPermissionInterceptor;
import com.wudgaby.starter.mybatisplus.helper.config.MyBatisConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/2 0002 11:37
 * @desc :
 */
@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class DataScopeConfig {

    @Bean
    public boolean datePermissionInit(MybatisPlusInterceptor mybatisPlusInterceptor) {
        List<InnerInterceptor> interceptors = new ArrayList<>(mybatisPlusInterceptor.getInterceptors());
        interceptors.add(new PlusDataPermissionInterceptor());
        mybatisPlusInterceptor.setInterceptors(interceptors);
        return true;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataScopeService dataScopeService() {
       throw new IllegalStateException("请实现DataScopeService接口");
    }
}
