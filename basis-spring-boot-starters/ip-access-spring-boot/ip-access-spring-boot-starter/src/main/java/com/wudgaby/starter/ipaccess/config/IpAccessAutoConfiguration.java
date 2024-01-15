package com.wudgaby.starter.ipaccess.config;

import com.wudgaby.starter.ipaccess.IpAccessFilter;
import com.wudgaby.starter.ipaccess.IpInitListener;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 10:12
 * @desc :
 */
@Configuration
@EnableConfigurationProperties(IpAccessProp.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "ip.access", value = "enabled", havingValue = "true", matchIfMissing = false)
@Import({CaffeineAutoConfiguration.class, RedisLettuceAutoConfiguration.class})
public class IpAccessAutoConfiguration {
    @Bean
    public IpInitListener ipInitListener() {
        return new IpInitListener();
    }

    /**
     * ip访问过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean ipAccessFilter(IpAccessProp ipAccessProp){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new IpAccessFilter(ipAccessProp.getStrategy()));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
