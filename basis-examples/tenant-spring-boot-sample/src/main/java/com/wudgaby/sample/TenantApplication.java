package com.wudgaby.sample;

import cn.hutool.extra.spring.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(SpringUtil.getBean("tenantInterceptor"))
                .addPathPatterns("/**");
    }
}
