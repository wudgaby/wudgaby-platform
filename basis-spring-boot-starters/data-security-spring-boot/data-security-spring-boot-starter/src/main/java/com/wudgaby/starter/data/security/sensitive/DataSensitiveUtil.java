package com.wudgaby.starter.data.security.sensitive;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wudgaby.starter.data.security.sensitive.annotation.Sensitive;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveJSONFieldKey;
import com.wudgaby.starter.data.security.sensitive.desensitize.DafaultDesensitizeHandler;
import com.wudgaby.starter.data.security.sensitive.desensitize.DesensitizeHandler;
import com.wudgaby.starter.data.security.sensitive.handler.SensitiveHandlerFactory;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.*;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/14 0014 17:31
 * @desc :
 */
@UtilityClass
public class DataSensitiveUtil {
    private final DesensitizeHandler DESENSITIZE_HANDLER = new DafaultDesensitizeHandler();

    public static <T> T sensitive(T t){
        if (t instanceof String) {
            return (T) DESENSITIZE_HANDLER.handle(t);
        }

        if (t instanceof Collection || t instanceof List){
            Collection collection = (Collection) t;
            if (collection.isEmpty() && collection.stream().findFirst().isPresent()) {
                return t;
            }

            Sensitive sensitive = collection.stream().findFirst().get().getClass().getAnnotation(Sensitive.class);
            if (sensitive == null) {
                return t;
            }

            collection.stream().forEach(item -> sensitiveObj(item));
        } else if (t.getClass().isArray()){
            T[] array = (T[])t;
            if (array.length == 0 && Arrays.stream(array).findFirst().isPresent()) {
                return t;
            }

            Sensitive sensitive = Arrays.stream(array).findFirst().get().getClass().getAnnotation(Sensitive.class);
            if (sensitive == null) {
                return t;
            }

            Arrays.stream(array).forEach(item -> sensitiveObj(item));
        } else {
            sensitiveObj(t);
        }
        return t;
    }

    private static <T> void sensitiveObj(T t){
        MetaObject metaObject = SystemMetaObject.forObject(t);
        Arrays.stream(t.getClass().getDeclaredFields())
                .filter(item -> Objects.nonNull(item.getAnnotation(SensitiveField.class)))
                .forEach(item -> {
                    String fieldName = item.getName();
                    Object sensitiveVal = SensitiveHandlerFactory.getSensitiveHandler(metaObject.getValue(fieldName), item.getAnnotation(SensitiveField.class))
                            .sensitive(metaObject.getValue(fieldName), item.getAnnotation(SensitiveField.class));
                    metaObject.setValue(fieldName, sensitiveVal);
                });
    }

    private boolean filter(Map.Entry<String, Object> item, SensitiveField sensitiveField){
        SensitiveJSONFieldKey[] fieldKeys = sensitiveField.jsonFieldKey();
        if(ObjectUtil.isNotNull(item) || ArrayUtil.isEmpty(fieldKeys)){
            return false;
        }
        return Arrays.stream(fieldKeys).filter(key -> StrUtil.equals(item.getKey(), key.key())).count() > 0;
    }
}
