package com.wudgaby.starter.data.security.dict;

import com.google.common.collect.Maps;
import com.wudgaby.starter.data.security.dict.annotation.DictBindBean;
import com.wudgaby.starter.data.security.dict.annotation.DictBindField;
import com.wudgaby.starter.data.security.dict.handler.DictHandlerFactory;
import com.wudgaby.starter.data.security.util.IgnoreClassUtil;
import lombok.experimental.UtilityClass;
import org.apache.commons.beanutils.ConvertUtils;
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
public class DictBindUtil {
    public static <T> T bind(T t){
        if (t instanceof Collection || t instanceof List){
            Collection collection = (Collection) t;
            if (collection.isEmpty() && collection.stream().findFirst().isPresent()) {
                return t;
            }

            DictBindBean dictBindBean = collection.stream().findFirst().get().getClass().getAnnotation(DictBindBean.class);
            if (dictBindBean == null) {
                return t;
            }

            Collection modifyParam = (Collection)t;
            modifyParam.stream().forEach(item -> bindObj(item));
            return (T)modifyParam;
        } else if (t.getClass().isArray()){
            T[] array = (T[])t;
            if (array.length == 0 && Arrays.stream(array).findFirst().isPresent()) {
                return t;
            }

            DictBindBean dictBindBean = Arrays.stream(array).findFirst().get().getClass().getAnnotation(DictBindBean.class);
            if (dictBindBean == null) {
                return t;
            }

            T[] modifyParam = (T[])t;
            Arrays.stream(modifyParam).forEach(item -> bindObj(item));
            return (T)modifyParam;
        } else {
            DictBindBean dictBindBean = t.getClass().getDeclaredAnnotation(DictBindBean.class);
            if (Objects.isNull(dictBindBean)) {
                return t;
            }

            T modifyParam = t;
            bindObj(modifyParam);
            return modifyParam;
        }
    }

    private static <T> void bindObj(T t){
        Map<String, DictBindField> dictBindFieldMap = getFieldMap(t);
        final MetaObject objMetaObject = SystemMetaObject.forObject(t);

        for (Map.Entry<String, DictBindField> entry : dictBindFieldMap.entrySet()) {
            String property = entry.getKey();
            Object value = objMetaObject.getValue(property);
            if (Objects.nonNull(value)) {
                Object transformed = DictHandlerFactory.getHandler(value, entry.getValue()).transform(value, entry.getValue());
                if(value instanceof String || IgnoreClassUtil.inIgnoreClass(value.getClass())){
                    Class targetClazz = getFieldClazz(t, entry.getValue().target());
                    Object convert = ConvertUtils.convert(transformed, targetClazz);
                    objMetaObject.setValue(entry.getValue().target(), convert);
                }else{
                    Class targetClazz = getFieldClazz(t, property);
                    Object convert = ConvertUtils.convert(transformed, targetClazz);
                    objMetaObject.setValue(property, convert);
                }
            }
        }
    }

    private static <T> Map<String, DictBindField> getFieldMap(T t) {
        Map<String, DictBindField> dictBindFieldMap = Maps.newHashMap();

        if (t == null) {
            return dictBindFieldMap;
        }

        for (Field field: t.getClass().getDeclaredFields()) {
            DictBindField dictBindField = field.getAnnotation(DictBindField.class);
            if (dictBindField != null) {
                dictBindFieldMap.put(field.getName(), dictBindField);
            }
        }
        return dictBindFieldMap;
    }

    private static <T> Class getFieldClazz(T t, String targetField) {
        if (t == null) {
            return null;
        }

        for (Field field: t.getClass().getDeclaredFields()) {
            if(field.getName().equals(targetField)){
                return field.getType();
            }
        }
        return String.class;
    }
}
