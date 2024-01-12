package com.wudgaby.starter.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wudgaby.starter.mybatisplus.helper.config.MyBatisConfig;
import com.wudgaby.starter.tenant.db.PlusTenantLineHandler;
import com.wudgaby.starter.tenant.job.TenantJobAspect;
import com.wudgaby.starter.tenant.service.TenantFrameworkService;
import com.wudgaby.starter.tenant.web.TenantContextWebFilter;
import com.wudgaby.starter.tenant.web.TenantSecurityWebFilter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/12/29 23:16
 * @desc :
 */
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
@AutoConfigureAfter(MyBatisConfig.class)
@ConditionalOnProperty(value = "tenant.enable", havingValue = "true", matchIfMissing = true)
public class TenantConfiguration {
    @Bean
    public boolean tenantInit(MybatisPlusInterceptor mybatisPlusInterceptor,
                              TenantProperties tenantProperties) {
        List<InnerInterceptor> interceptors = new ArrayList<>();
        // 多租户插件 必须放到第一位
        interceptors.add(new TenantLineInnerInterceptor(new PlusTenantLineHandler(tenantProperties)));
        interceptors.addAll(mybatisPlusInterceptor.getInterceptors());
        mybatisPlusInterceptor.setInterceptors(interceptors);
        return true;
    }

    @Bean
    public TenantJobAspect tenantJobAspect(TenantFrameworkService tenantFrameworkService) {
        return new TenantJobAspect(tenantFrameworkService);
    }

    /**
     * 从head获取租户id
     */
    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
        FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantContextWebFilter());
        registrationBean.setOrder(-100);
        return registrationBean;
    }

    /**
     *
     */
    @Bean
    public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter(TenantProperties tenantProperties,
                                                                                   TenantFrameworkService tenantFrameworkService) {
        FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantSecurityWebFilter(tenantProperties, tenantFrameworkService));
        registrationBean.setOrder(-99);
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantFrameworkService tenantFrameworkService() {
        throw new IllegalArgumentException("请先实现TenantFrameworkService");
    }
}
