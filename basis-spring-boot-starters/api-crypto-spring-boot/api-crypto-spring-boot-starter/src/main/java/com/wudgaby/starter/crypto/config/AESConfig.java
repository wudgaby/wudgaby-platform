package com.wudgaby.starter.crypto.config;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.wudgaby.starter.crypto.util.AESUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/4 0004 19:35
 * @desc :
 */
@EnableConfigurationProperties(CryptoProperties.class)
public class AESConfig {
    @Resource
    private CryptoProperties cryptoProperties;

    @Bean
    public SymmetricCrypto symmetricCrypto(){
        SymmetricCrypto crypto;
        if(BooleanUtil.isTrue(cryptoProperties.getSimple())){
            crypto = new SymmetricCrypto(SymmetricAlgorithm.AES, cryptoProperties.getKey().getBytes(StandardCharsets.UTF_8));
        } else {
            crypto = new AES(cryptoProperties.getCustom().getMode(), cryptoProperties.getCustom().getPadding(), cryptoProperties.getCustom().getKey().getBytes(StandardCharsets.UTF_8), cryptoProperties.getCustom().getIv().getBytes(StandardCharsets.UTF_8));
        }
        AESUtil.init(crypto);
        return crypto;
    }
}
