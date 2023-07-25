package com.wudgaby.starter.data.security.crypt.handler;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import com.wudgaby.starter.data.security.exception.DataCryptException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * * 处理 bean 实体的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeanCryptHandler implements CryptHandler<Object> {
    private boolean keepBean;
    private static final ConcurrentHashMap<Class, List<CryptoFieldVo>> CLASS_ENCRYPT_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class, List<CryptoFieldVo>> CLASS_DECRYPT_MAP = new ConcurrentHashMap<>();

    @Override
    public Object encrypt(Object bean, CryptoField cryptoField) {
        if (bean == null) {
            return null;
        }
        Object result;
        if(BooleanUtil.isFalse(keepBean)){
            try {
                result = ObjectUtil.cloneByStream(bean);
            } catch (Exception e) {
                throw new DataCryptException(e);
            }
        }else {
            result = bean;
        }

        List<CryptoFieldVo> filedList = CLASS_ENCRYPT_MAP.computeIfAbsent(result.getClass(), this::getEncryptFields);
        filedList.forEach(cryptFiled -> {
            try {
                cryptFiled.field.setAccessible(true);
                Object obj = cryptFiled.field.get(result);
                if (obj != null) {
                    Object encrypted = CryptHandlerFactory.getCryptHandler(obj, cryptFiled.cryptField).encrypt(obj, cryptFiled.cryptField);
                    cryptFiled.field.set(result, encrypted);
                }
            } catch (IllegalAccessException e) {
                throw new DataCryptException(e);
            }
        });
        return result;
    }

    private List<CryptoFieldVo> getEncryptFields(Class cls) {
        List<CryptoFieldVo> filedList = new ArrayList<>();
        if (cls == null) {
            return filedList;
        }

        Field[] objFields = cls.getDeclaredFields();
        for (Field field : objFields) {
            CryptoField cryptField = field.getAnnotation(CryptoField.class);
            if (cryptField != null) {
                filedList.add(new CryptoFieldVo(cryptField, field));
            }
        }
        return filedList;
    }

    private List<CryptoFieldVo> getDecryptFields(Class cls) {
        List<CryptoFieldVo> filedList = new ArrayList<>();
        if (cls == null) {
            return filedList;
        }

        Field[] objFields = cls.getDeclaredFields();
        for (Field field : objFields) {
            CryptoField cryptField = field.getAnnotation(CryptoField.class);
            if (cryptField != null) {
                filedList.add(new CryptoFieldVo(cryptField, field));
            }
        }
        return filedList;
    }

    @Override
    public Object decrypt(Object param, CryptoField cryptoField) {
        if (param == null) {
            return null;
        }
        List<CryptoFieldVo> filedList = CLASS_DECRYPT_MAP.computeIfAbsent(param.getClass(), this::getDecryptFields);
        filedList.forEach(cryptFiled -> {
            try {
                cryptFiled.field.setAccessible(true);
                Object obj = cryptFiled.field.get(param);
                if (obj != null) {
                    Object decrypted = CryptHandlerFactory.getCryptHandler(obj, cryptFiled.cryptField).decrypt(obj, cryptFiled.cryptField);
                    cryptFiled.field.set(param, decrypted);
                }
            } catch (IllegalAccessException e) {
                throw new DataCryptException(e);
            }
        });
        return param;
    }

    private class CryptoFieldVo {
        private CryptoFieldVo(CryptoField cryptField, Field field) {
            this.cryptField = cryptField;
            this.field = field;
        }

        private Field field;
        private CryptoField cryptField;
    }
}
