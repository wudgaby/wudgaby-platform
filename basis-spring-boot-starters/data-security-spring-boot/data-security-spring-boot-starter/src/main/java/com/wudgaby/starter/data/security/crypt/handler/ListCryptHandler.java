package com.wudgaby.starter.data.security.crypt.handler;

import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理 List 对象的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public class ListCryptHandler implements CryptHandler<List> {

    @Override
    public Object encrypt(List param, CryptoField cryptoField) {
        if (!needCrypt(param)) {
            return param;
        }
        return param.stream()
            .map(item -> CryptHandlerFactory.getCryptHandler(item, cryptoField).encrypt(item, cryptoField))
            .collect(Collectors.toList());
    }

    @Override
    public Object decrypt(List param, CryptoField cryptoField) {
        if (!needCrypt(param)) {
            return param;
        }
        return param.stream()
            .map(item -> CryptHandlerFactory.getCryptHandler(item, cryptoField).decrypt(item, cryptoField))
            .collect(Collectors.toList());
    }

    private boolean needCrypt(List list) {
        return list != null && list.size() != 0;
    }
}
