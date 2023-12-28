package com.wudgaby.platform.permission.configuartion;

import com.wudgaby.platform.permission.filter.AccessFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/31 16:08
 * @Desc :   
 */
@Configuration
public class AccessFilterConfiguration {

    @Bean
    public FilterRegistrationBean accessFilterBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(accessFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public Filter accessFilter(){
        return new AccessFilter();
    }

}
