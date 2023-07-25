package com.wudgaby.starter.data.security.sensitive.handler;

import cn.hutool.core.util.ArrayUtil;
import com.wudgaby.starter.data.security.enums.HandlerType;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;
import com.wudgaby.starter.data.security.util.CryptUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脱敏处理注册表
 * @author wudgaby
 */
public class SensitiveHandlerFactory {
    private static final Map<HandlerType, SensitiveHandler> HANDLER_MAP = new ConcurrentHashMap<>();
    static {
        HANDLER_MAP.put(HandlerType.STRING, new StringSensitiveHandler());
        HANDLER_MAP.put(HandlerType.COLLECTION, new CollectionSensitiveHandler());
        HANDLER_MAP.put(HandlerType.LIST, new ListSensitiveHandler());
        HANDLER_MAP.put(HandlerType.ARRAY, new ArraySensitiveHandler());
        HANDLER_MAP.put(HandlerType.BEAN, new BeanSensitiveHandler());
        HANDLER_MAP.put(HandlerType.DEFAULT, new EmptySensitiveHandler());
        HANDLER_MAP.put(HandlerType.JSON, new JsonSensitiveHandler());
        HANDLER_MAP.put(HandlerType.MAP, new MapSensitiveHandler());
    }

    public static SensitiveHandler getSensitiveHandler(Object obj, SensitiveField sensitiveField) {
        if (obj == null || CryptUtil.inIgnoreClass(obj.getClass())) {
            return HANDLER_MAP.get(HandlerType.DEFAULT);
        }

        if (obj instanceof Map) {
            if(ArrayUtil.isEmpty(sensitiveField.jsonFieldKey())){
                return HANDLER_MAP.get(HandlerType.DEFAULT);
            }else{
                return HANDLER_MAP.get(HandlerType.MAP);
            }
        }

        if (obj instanceof String && sensitiveField == null) {
            return HANDLER_MAP.get(HandlerType.DEFAULT);
        }

        if (obj instanceof String && ArrayUtil.isNotEmpty(sensitiveField.jsonFieldKey())) {
            return HANDLER_MAP.get(HandlerType.JSON);
        }

        if (obj instanceof String) {
            return HANDLER_MAP.get(HandlerType.STRING);
        }

        if (obj instanceof List) {
            return HANDLER_MAP.get(HandlerType.LIST);
        }

        if (obj instanceof Collection) {
            return HANDLER_MAP.get(HandlerType.COLLECTION);
        }

        if (obj.getClass().isArray()) {
            return HANDLER_MAP.get(HandlerType.ARRAY);
        }

        return HANDLER_MAP.get(HandlerType.BEAN);
    }
}
