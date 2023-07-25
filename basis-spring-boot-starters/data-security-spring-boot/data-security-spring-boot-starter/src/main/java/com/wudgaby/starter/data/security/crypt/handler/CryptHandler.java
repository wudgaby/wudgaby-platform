package com.wudgaby.starter.data.security.crypt.handler;

import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;

/**
 * 加解密处理抽象类
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public interface CryptHandler<T> {
    /**
     * 加密
     * @param param
     * @param cryptoField
     * @return
     */
    Object encrypt(T param, CryptoField cryptoField);

    /**
     * 解密
     * @param param
     * @param cryptoField
     * @return
     */
    Object decrypt(T param, CryptoField cryptoField);
}
