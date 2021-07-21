package com.wudgaby.platform.permission.configuartion;

import com.wudgaby.platform.permission.filter.AccessFilter;
import com.wudgaby.platform.webcore.configuration.CorsProperties;
import com.wudgaby.platform.webcore.security.CsrfFilter;
import com.wudgaby.platform.webcore.security.xss.XssFilter;
import com.wudgaby.platform.webcore.support.RunLogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName : WebMvcConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/31 16:08
 * @Desc :   
 */
@Configuration
public class AccessFilterConfiguration {

    @Bean
    public FilterRegistrationBean accessFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AccessFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
