package com.wudgaby.sample;

import cn.hutool.extra.spring.SpringUtil;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.starter.tenant.web.TenantSecurityInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/3 0003 11:22
 * @desc :
 */
@SpringBootApplication(scanBasePackages = {"com.wudgaby.sample"})
@MapperScan("com.wudgaby.sample.mapper")
public class TenantApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(TenantApplication.class, args);
    }

    @Autowired
    private TenantSecurityInterceptor tenantSecurityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Set<String> excludePatterns = ExcludeRegistry.ofStaticResource().excludePathPatterns("/sysUsers/login").getAllExcludePatterns();
        registry.addInterceptor(SpringUtil.getBean("tenantInterceptor"))
                .addPathPatterns("/**")
                .excludePathPatterns(new ArrayList<>(excludePatterns));

        registry.addInterceptor(tenantSecurityInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(new ArrayList<>(excludePatterns));
    }
}
