package com.wudgaby.starter.crypto;

import com.wudgaby.starter.crypto.config.AESConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/5 0005 11:58
 * @desc :
 */
@Configuration
@Import(AESConfig.class)
public class ApiCryptoAutoConfiguration {
    /*@Bean
    public RequestBodyAdviceAdapter requestBodyAdviceAdapter(){
        return new DecryptionRequestBodyAdvice();
    }

    @Bean
    public ResponseBodyAdvice responseBodyAdvice(){
        return new EncryptionResponseBodyAdvice();
    }*/
}
