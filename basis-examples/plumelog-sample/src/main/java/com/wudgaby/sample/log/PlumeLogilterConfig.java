package com.wudgaby.sample.log;

import com.plumelog.core.TraceIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/29 0029 16:12
 * @desc :
 */
@Configuration
public class PlumeLogilterConfig {
    @Bean
    public FilterRegistrationBean filterRegistrationBean1() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(initCustomFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Integer.MIN_VALUE);
        return filterRegistrationBean;
    }

    @Bean
    public Filter initCustomFilter() {
        return new TraceIdFilter();
    }
}