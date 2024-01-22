package com.wudgaby.starter.license.verify.config;

import cn.hutool.extra.spring.SpringUtil;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.starter.license.verify.LicenseVerify;
import com.wudgaby.starter.license.verify.aspect.LicenseVerifyInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/22 0022 12:22
 * @desc :
 */
@Configuration
@RequiredArgsConstructor
public class LicenseWebConfiguration implements WebMvcConfigurer {
    private final LicenseProp licenseProp;

    /*@Bean
    public FilterRegistrationBean<LicenseVerifyFilter> licenseVerifyFilter(LicenseProp licenseProp, LicenseVerify leLicenseVerify) {
        FilterRegistrationBean<LicenseVerifyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LicenseVerifyFilter(licenseProp,  leLicenseVerify));
        registrationBean.setOrder(-200);
        return registrationBean;
    }*/

    @Bean
    @ConditionalOnMissingBean
    public LicenseVerifyInterceptor licenseVerifyInterceptor(LicenseProp licenseProp, LicenseVerify leLicenseVerify) {
        return new LicenseVerifyInterceptor(licenseProp, leLicenseVerify);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Set<String> excludePatterns = ExcludeRegistry.ofStaticResource()
                .excludePathPatterns(licenseProp.getImportLicenseUrl())
                .getAllExcludePatterns();

        registry.addInterceptor(SpringUtil.getBean("licenseVerifyInterceptor"))
                .addPathPatterns("/**")
                .excludePathPatterns(new ArrayList<>(excludePatterns));
    }
}
