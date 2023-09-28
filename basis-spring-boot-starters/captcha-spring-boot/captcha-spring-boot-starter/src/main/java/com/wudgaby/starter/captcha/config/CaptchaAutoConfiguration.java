package com.wudgaby.starter.captcha.config;

import com.wudgaby.starter.captcha.core.dao.CaptchaStoreDao;
import com.wudgaby.starter.captcha.dao.MemoryCaptchaStoreDao;
import com.wudgaby.starter.captcha.dao.SessionCaptchaStoreDao;
import com.wudgaby.starter.captcha.filter.CaptchaFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 10:08
 * @desc :
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CaptchaProp.class)
@ConditionalOnWebApplication
public class CaptchaAutoConfiguration{
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "captcha.storeType", havingValue = "SESSION")
    public CaptchaStoreDao sessionCaptchaStoreDao(){
        return new SessionCaptchaStoreDao();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "captcha.storeType", havingValue = "MEMORY")
    public CaptchaStoreDao memoryCaptchaStoreDao(){
        return new MemoryCaptchaStoreDao();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "captcha.storeType", havingValue = "REDIS")
    public CaptchaStoreDao redisStoreDao(){
        throw new IllegalArgumentException("使用redis存储.maven请集成 captcha-dao-redis");
    }

    @Bean
    @ConditionalOnMissingBean
    public CaptchaEndpoint captchaEndpoint(CaptchaProp captchaProp, CaptchaStoreDao captchaStoreDao) {
        return new CaptchaEndpoint(captchaProp, captchaStoreDao);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "captcha.filter-mode.enabled", havingValue = "true")
    public FilterRegistrationBean captchaFilter(CaptchaProp captchaProp, CaptchaStoreDao captchaStoreDao){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CaptchaFilter(captchaProp, captchaStoreDao));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MAX_VALUE);
        return registrationBean;
    }
}
