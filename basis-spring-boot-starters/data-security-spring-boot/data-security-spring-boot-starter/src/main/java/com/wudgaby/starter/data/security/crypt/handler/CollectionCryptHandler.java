package com.wudgaby.starter.data.security.crypt.handler;

import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * * 处理 Collection 对象的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public class CollectionCryptHandler implements CryptHandler<Collection> {

    @Override
    public Object encrypt(Collection collection, CryptoField cryptoField) {
        if (!needCrypt(collection)) {
            return collection;
        }
        return collection.stream()
            .map(item -> CryptHandlerFactory.getCryptHandler(item, cryptoField).encrypt(item, cryptoField))
            .collect(Collectors.toList());
    }

    @Override
    public Object decrypt(Collection param, CryptoField cryptoField) {
        if (!needCrypt(param)) {
            return param;
        }
        return param.stream()
            .map(item -> CryptHandlerFactory.getCryptHandler(item, cryptoField).decrypt(item, cryptoField))
            .collect(Collectors.toList());
    }


    private boolean needCrypt(Collection collection) {
        return collection != null && collection.size() != 0;
    }
}
