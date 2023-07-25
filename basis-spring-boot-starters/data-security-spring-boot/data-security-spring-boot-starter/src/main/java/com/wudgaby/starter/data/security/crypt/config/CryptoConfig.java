package com.wudgaby.starter.data.security.crypt.config;


import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.wudgaby.starter.data.security.crypt.advice.DecryptionRequestBodyAdvice;
import com.wudgaby.starter.data.security.crypt.advice.EncryptionResponseBodyAdvice;
import com.wudgaby.starter.data.security.crypt.encrypt.CryptoHandler;
import com.wudgaby.starter.data.security.crypt.encrypt.DefaultCryptoHandler;
import com.wudgaby.starter.data.security.crypt.handler.BeanCryptHandler;
import com.wudgaby.starter.data.security.crypt.handler.CryptHandlerFactory;
import com.wudgaby.starter.data.security.crypt.handler.StringCryptHandler;
import com.wudgaby.starter.data.security.enums.HandlerType;
import com.wudgaby.starter.data.security.util.AESUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author wudgaby
 */
@EnableConfigurationProperties(DataCryptoProperties.class)
@Configuration
public class CryptoConfig {
    @Resource
    private DataCryptoProperties dataCryptoProperties;

    @Bean
    @ConditionalOnMissingBean
    public CryptoHandler defaultCryptoHandler(){
        return new DefaultCryptoHandler();
    }

    @PostConstruct
    public SymmetricCrypto symmetricCrypto(){
        SymmetricCrypto crypto;
        if(dataCryptoProperties.isSimple()){
            crypto = new SymmetricCrypto(SymmetricAlgorithm.AES, dataCryptoProperties.getKey().getBytes(StandardCharsets.UTF_8));
        } else {
            crypto = new AES(dataCryptoProperties.getCustom().getMode(), dataCryptoProperties.getCustom().getPadding(),
                    dataCryptoProperties.getCustom().getKey().getBytes(StandardCharsets.UTF_8),
                    dataCryptoProperties.getCustom().getIv().getBytes(StandardCharsets.UTF_8));
        }
        AESUtil.init(crypto);
        return crypto;
    }

    @Bean
    @ConditionalOnMissingBean
    public CryptHandlerFactory cryptHandlerFactory(CryptoHandler encrypt){
        CryptHandlerFactory.put(HandlerType.STRING, new StringCryptHandler(encrypt));
        CryptHandlerFactory.put(HandlerType.BEAN, new BeanCryptHandler(dataCryptoProperties.isKeepBean()));
        return new CryptHandlerFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public EncryptionResponseBodyAdvice encryptionResponseBodyAdvice(){
        return new EncryptionResponseBodyAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    public DecryptionRequestBodyAdvice decryptionRequestBodyAdvice(){
        return new DecryptionRequestBodyAdvice();
    }
}
