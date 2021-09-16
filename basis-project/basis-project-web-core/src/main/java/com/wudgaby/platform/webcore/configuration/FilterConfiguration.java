package com.wudgaby.platform.webcore.configuration;

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
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new RunLogFilter());
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean corsFilterBean(CorsFilter globalCorsFilter){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(globalCorsFilter);
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    @Bean
    public CorsFilter globalCorsFilter(CorsConfigurationSource corsConfigurationSource){
        return new CorsFilter(corsConfigurationSource);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(corsProperties.isAllowCredentials());
        config.setMaxAge(corsProperties.getMaxAge().getSeconds());
        config.setAllowedOrigins(corsProperties.getAllowedOrigins());
        config.setAllowedHeaders(corsProperties.getAllowedHeaders());
        config.setAllowedMethods(corsProperties.getAllowedMethods());
        source.registerCorsConfiguration(corsProperties.getAntPath(), config);
        return source;
    }

    @Bean
    @ConditionalOnProperty(value = "csrf.enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean csrfFilterBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CsrfFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(value = "xss.enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean xssFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new XssFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(3);
        return registrationBean;
    }

}
