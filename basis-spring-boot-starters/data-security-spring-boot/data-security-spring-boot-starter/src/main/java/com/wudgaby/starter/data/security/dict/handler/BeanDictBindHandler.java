package com.wudgaby.starter.data.security.dict.handler;

import com.wudgaby.starter.data.security.dict.annotation.DictBindField;
import com.wudgaby.starter.data.security.exception.DictTransformException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Data
@NoArgsConstructor
public class BeanDictBindHandler implements DictBindHandler<Object> {
    private static final ConcurrentHashMap<Class, List<DictFieldVo>> CLASS_ENCRYPT_MAP = new ConcurrentHashMap<>();

    @Override
    public Object transform(Object bean, DictBindField dictBindField) {
        if (bean == null) {
            return null;
        }
        Object result = bean;
        List<DictFieldVo> filedList = CLASS_ENCRYPT_MAP.computeIfAbsent(result.getClass(), this::getFields);
        filedList.forEach(dictFiled -> {
            try {
                dictFiled.field.setAccessible(true);
                Object obj = dictFiled.field.get(result);
                if (obj != null) {
                    Object transformed = DictHandlerFactory.getHandler(obj, dictFiled.dictBindField).transform(obj, dictFiled.dictBindField);

                    Field targetField = result.getClass().getField(dictFiled.dictBindField.target());
                    targetField.set(result, transformed);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new DictTransformException(e);
            }
        });
        return result;
    }

    private List<DictFieldVo> getFields(Class cls) {
        List<DictFieldVo> filedList = new ArrayList<>();
        if (cls == null) {
            return filedList;
        }

        Field[] objFields = cls.getDeclaredFields();
        for (Field field : objFields) {
            DictBindField cryptField = field.getAnnotation(DictBindField.class);
            if (cryptField != null) {
                filedList.add(new DictFieldVo(cryptField, field));
            }
        }
        return filedList;
    }

    private class DictFieldVo {
        private DictFieldVo(DictBindField dictBindField, Field field) {
            this.dictBindField = dictBindField;
            this.field = field;
        }

        private Field field;
        private DictBindField dictBindField;
    }
}
