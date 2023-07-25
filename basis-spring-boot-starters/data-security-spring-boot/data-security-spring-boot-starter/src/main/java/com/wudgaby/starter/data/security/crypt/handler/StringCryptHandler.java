package com.wudgaby.starter.data.security.crypt.handler;

import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import com.wudgaby.starter.data.security.crypt.encrypt.CryptoHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 处理 String 对象的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringCryptHandler implements CryptHandler<String> {
    private CryptoHandler cryptoHandler;

    @Override
    public Object encrypt(String param, CryptoField cryptoField) {
        if (needCrypt(param)) {
            return cryptoHandler.encrypt(param);
        }
        return param;
    }

    private boolean needCrypt(String param) {
        return cryptoHandler !=null && param != null && param.length() != 0;
    }

    @Override
    public Object decrypt(String param, CryptoField cryptoField) {
        if (needCrypt(param)) {
            return cryptoHandler.decrypt(param);
        }
        return param;
    }
}

