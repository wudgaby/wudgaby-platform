package com.wudgaby.starter.data.security.sensitive.handler;

import com.wudgaby.starter.data.security.exception.SensitiveException;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理 bean 实体的脱敏
 * @author wudgaby
 */
@Data
@NoArgsConstructor
public class BeanSensitiveHandler implements SensitiveHandler<Object> {
    private static final ConcurrentHashMap<Class, List<SensitiveFieldVo>> CLASS_ENCRYPT_MAP = new ConcurrentHashMap<>();

    @Override
    public Object sensitive(Object bean, SensitiveField sensitiveField) {
        if (bean == null) {
            return null;
        }

        List<SensitiveFieldVo> filedList = CLASS_ENCRYPT_MAP.computeIfAbsent(bean.getClass(), this::getSensitiveFields);
        filedList.forEach(sensitiveFieldVo -> {
            try {
                sensitiveFieldVo.field.setAccessible(true);
                Object obj = sensitiveFieldVo.field.get(bean);
                if (obj != null) {
                    Object encrypted = SensitiveHandlerFactory.getSensitiveHandler(obj, sensitiveFieldVo.sensitiveField).sensitive(obj, sensitiveFieldVo.sensitiveField);
                    sensitiveFieldVo.field.set(bean, encrypted);
                }
            } catch (IllegalAccessException e) {
                throw new SensitiveException(e);
            }
        });
        return bean;
    }

    private List<SensitiveFieldVo> getSensitiveFields(Class cls) {
        List<SensitiveFieldVo> filedList = new ArrayList<>();
        if (cls == null) {
            return filedList;
        }

        Field[] objFields = cls.getDeclaredFields();
        for (Field field : objFields) {
            SensitiveField cryptField = field.getAnnotation(SensitiveField.class);
            if (cryptField != null) {
                filedList.add(new SensitiveFieldVo(cryptField, field));
            }
        }
        return filedList;
    }

    private class SensitiveFieldVo {
        private SensitiveFieldVo(SensitiveField sensitiveField, Field field) {
            this.sensitiveField = sensitiveField;
            this.field = field;
        }

        private Field field;
        private SensitiveField sensitiveField;
    }
}
