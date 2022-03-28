package com.wudgaby.platform.webcore.configuration;

import com.wudgaby.platform.webcore.filter.RunLogFilter;
import com.wudgaby.platform.webcore.filter.TraceFilter;
import com.wudgaby.platform.webcore.security.CsrfFilter;
import com.wudgaby.platform.webcore.security.xss.XssFilter;
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

    /**
     * 请求跟踪id过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean traceFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new TraceFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    /**
     * 请求路径/时长记录过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean runLogFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new RunLogFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    /**
     * 跨域过滤器
     * @param corsProperties
     * @return
     */
    @Bean
    public FilterRegistrationBean corsFilterBean(CorsProperties corsProperties){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CorsFilter(corsConfigurationSource(corsProperties)));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(corsProperties.isAllowCredentials());
        config.setMaxAge(corsProperties.getMaxAge().getSeconds());
        config.addAllowedOrigin(corsProperties.getAllowedOrigins());
        config.addAllowedHeader(corsProperties.getAllowedHeaders());
        config.addAllowedMethod(corsProperties.getAllowedMethods());
        source.registerCorsConfiguration(corsProperties.getAntPath(), config);
        return source;
    }

    /**
     * csrf攻击过滤器
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "csrf.enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean csrfFilterBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CsrfFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    /**
     * xss攻击过滤器
     * @return
     */
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
