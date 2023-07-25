package com.wudgaby.starter.data.security.crypt.handler;

import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;

import java.util.Arrays;

/**
 * 处理 Array 对象的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public class ArrayCryptHandler implements CryptHandler<Object> {

    @Override
    public Object encrypt(Object object, CryptoField cryptoField) {
        if (object == null) {
            return null;
        }
        return Arrays.stream((Object[]) object)
            .map(item -> CryptHandlerFactory.getCryptHandler(item, cryptoField).encrypt(item, cryptoField))
            .toArray();
    }

    @Override
    public Object decrypt(Object param, CryptoField cryptoField) {
        if (param == null) {
            return null;
        }
        return Arrays.stream((Object[]) param)
            .map(item -> CryptHandlerFactory.getCryptHandler(item, cryptoField).decrypt(item, cryptoField))
            .toArray();
    }


}
