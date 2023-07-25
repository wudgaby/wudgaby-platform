package com.wudgaby.starter.data.security.crypt.config;

import com.wudgaby.starter.data.security.crypt.intercept.CryptParamInterceptor;
import com.wudgaby.starter.data.security.crypt.intercept.CryptReadInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/25 0025 9:29
 * @desc :
 */
@Configuration
@ConditionalOnProperty(value = "crypto.data.db-crypto", havingValue = "true", matchIfMissing = false)
public class MyBatisCryptoConfig {
    @Bean
    @ConditionalOnMissingBean
    public CryptParamInterceptor cryptParamInterceptor(){
        return new CryptParamInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public CryptReadInterceptor cryptReadInterceptor(){
        return new CryptReadInterceptor();
    }
}
