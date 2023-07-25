package com.wudgaby.starter.data.security.crypt.handler;

import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;

/**
 * 空的加解密执行者，避免过多空指针判断
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public class EmptyCryptHandler implements CryptHandler<Object> {

    @Override
    public Object encrypt(Object param, CryptoField cryptoField) {
        return param;
    }

    @Override
    public Object decrypt(Object param, CryptoField cryptoField) {
        return param;
    }
}
