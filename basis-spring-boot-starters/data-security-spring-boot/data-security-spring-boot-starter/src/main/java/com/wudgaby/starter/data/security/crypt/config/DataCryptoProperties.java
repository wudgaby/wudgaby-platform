package com.wudgaby.starter.data.security.crypt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/12 0012 18:31
 * @desc :
 */
@Data
@ConfigurationProperties(prefix = "crypto.data")
public class DataCryptoProperties {
    /**
     * 加密秘钥
     */
    private String key;

    /**
     * 加密时,是否不改变源对象
     */
    private boolean keepBean;

    private boolean simple;

    private AesProp custom;

    /**
     * 开启数据库加解密
     */
    private boolean dbCrypto;

    /**
     * 开启数据返回字典绑定
     */
    private boolean dbDictBind;

    @Data
    public static class AesProp {
        private String mode;
        private String padding;
        private String key;
        private String iv;
    }
}
