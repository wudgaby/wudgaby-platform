package com.wudgaby.starter.data.security.crypt;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoBean;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import com.wudgaby.starter.data.security.crypt.handler.CryptHandlerFactory;
import com.wudgaby.starter.data.security.enums.HandlerType;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/14 0014 16:38
 * @desc :
 */
@UtilityClass
public class DataCryptUtil {
    public static <T> T encrypt(T t){
        return encrypt(t, false);
    }

    public static <T> T encrypt(T t, boolean keepBean){
        if (t instanceof Collection || t instanceof List){
            Collection collection = (Collection) t;
            if (collection.isEmpty() && collection.stream().findFirst().isPresent()) {
                return t;
            }

            CryptoBean cryptoBean = collection.stream().findFirst().get().getClass().getAnnotation(CryptoBean.class);
            if (cryptoBean == null) {
                return t;
            }

            Collection modifyParam = (Collection)t;
            if(BooleanUtil.isTrue(keepBean)) {
                modifyParam = (Collection)ObjectUtil.cloneByStream(t);
            }

            modifyParam.stream().forEach(item -> encryptObj(item));
            return (T)modifyParam;
        } else if (t.getClass().isArray()){
            T[] array = (T[])t;
            if (array.length == 0 && Arrays.stream(array).findFirst().isPresent()) {
                return t;
            }

            CryptoBean cryptoBean = Arrays.stream(array).findFirst().get().getClass().getAnnotation(CryptoBean.class);
            if (cryptoBean == null) {
                return t;
            }

            T[] modifyParam = (T[])t;
            if(BooleanUtil.isTrue(keepBean)) {
                modifyParam =  (T[])ObjectUtil.cloneByStream(t);
            }
            Arrays.stream(modifyParam).forEach(item -> encryptObj(item));
            return (T)modifyParam;
        } else {
            CryptoBean cryptoBeanAnnotation = t.getClass().getDeclaredAnnotation(CryptoBean.class);
            if (Objects.isNull(cryptoBeanAnnotation)) {
                return t;
            }

            T modifyParam = t;
            if(BooleanUtil.isTrue(keepBean)) {
                modifyParam = ObjectUtil.cloneByStream(t);
            }
            encryptObj(modifyParam);
            return modifyParam;
        }
    }

    private static <T> void encryptObj(T t){
        MetaObject metaObject = SystemMetaObject.forObject(t);
        Arrays.stream(t.getClass().getDeclaredFields())
                .filter(item -> Objects.nonNull(item.getAnnotation(CryptoField.class)) && item.getAnnotation(CryptoField.class).encrypt())
                .forEach(item -> {
                    String fieldName = item.getName();
                    cryptField(fieldName, metaObject, item.getAnnotation(CryptoField.class));
                });

    }

    public static <T> T decrypt(T t){
        if (t instanceof String) {
            return (T)CryptHandlerFactory.get(HandlerType.STRING).decrypt(t, null);
        }

        if (t instanceof Collection || t instanceof List){
            Collection collection = (Collection) t;
            if (collection.isEmpty() && collection.stream().findFirst().isPresent()) {
                return t;
            }

            CryptoBean cryptoBean = collection.stream().findFirst().get().getClass().getAnnotation(CryptoBean.class);
            if (cryptoBean == null) {
                return t;
            }

            collection.stream().forEach(item -> decryptObj(item));
        } else if (t.getClass().isArray()){
            T[] array = (T[])t;
            if (array.length == 0 && Arrays.stream(array).findFirst().isPresent()) {
                return t;
            }

            CryptoBean cryptoBean = Arrays.stream(array).findFirst().get().getClass().getAnnotation(CryptoBean.class);
            if (cryptoBean == null) {
                return t;
            }

            Arrays.stream(array).forEach(item -> decryptObj(item));
        } else {
            CryptoBean cryptoBeanAnnotation = t.getClass().getDeclaredAnnotation(CryptoBean.class);
            if (Objects.isNull(cryptoBeanAnnotation)) {
                return t;
            }
            decryptObj(t);
        }
        return t;
    }

    private static <T> void decryptObj(T t){
        Map<String, CryptoField> cryptFieldMap = getCryptFieldMap(t);
        final MetaObject objMetaObject = SystemMetaObject.forObject(t);

        for (Map.Entry<String, CryptoField> entry : cryptFieldMap.entrySet()) {
            String property = entry.getKey();
            Object value = objMetaObject.getValue(property);
            if (Objects.nonNull(value)) {
                objMetaObject.setValue(property, CryptHandlerFactory.getCryptHandler(value, entry.getValue()).decrypt(value, entry.getValue()));
            }
        }
    }

    private static <T> Map<String, CryptoField> getCryptFieldMap(T t) {
        Map<String, CryptoField> cryptFieldMap = Maps.newHashMap();

        if (t == null) {
            return cryptFieldMap;
        }

        for (Field field: t.getClass().getDeclaredFields()) {
            CryptoField cryptoField = field.getAnnotation(CryptoField.class);
            if (cryptoField != null && cryptoField.decrypt()) {
                cryptFieldMap.put(field.getName(), cryptoField);
            }
        }
        return cryptFieldMap;
    }

    private static void cryptField(String fieldName, MetaObject metaObject, CryptoField cryptoField){
        Object fieldVal = metaObject.getValue(fieldName);
        Object encryptVal = CryptHandlerFactory.getCryptHandler(fieldVal, cryptoField).encrypt(fieldVal, cryptoField);
        metaObject.setValue(fieldName, encryptVal);
    }
}
