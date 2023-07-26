package com.wudgaby.starter.data.security.dict.handler;

import com.google.common.collect.Maps;
import com.wudgaby.starter.data.security.dict.annotation.DictBindField;
import com.wudgaby.starter.data.security.enums.HandlerType;
import com.wudgaby.starter.data.security.util.IgnoreClassUtil;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 加解密处理者工厂类
 *
 * @author junliang.zhuo
 * @date 2019-03-29 13:02
 */
public class DictHandlerFactory {
    private static final Map<HandlerType, DictBindHandler> HANDLER_MAP  = Maps.newHashMap();
    static {
        HANDLER_MAP.put(HandlerType.STRING, new StringDictBindHandler());
        HANDLER_MAP.put(HandlerType.COLLECTION, new CollectionDictBindHandler());
        HANDLER_MAP.put(HandlerType.LIST, new ListDictBindHandler());
        HANDLER_MAP.put(HandlerType.ARRAY, new ArrayDictBindHandler());
        HANDLER_MAP.put(HandlerType.BEAN, new BeanDictBindHandler());
        HANDLER_MAP.put(HandlerType.DEFAULT, new EmptyDictBindHandler());
    }

    public static void put(@NonNull HandlerType type, @NonNull DictBindHandler handler) {
        HANDLER_MAP.put(type, handler);
    }
    public static DictBindHandler get(HandlerType type) {
        DictBindHandler cryptHandler = HANDLER_MAP.get(type);
        if(cryptHandler == null){
            throw new IllegalArgumentException(type + " 无有效的加解密处理者.");
        }
        return cryptHandler;
    }

    public static DictBindHandler getHandler(Object obj, DictBindField dictBindField) {
        if (obj == null) {
            return HANDLER_MAP.get(HandlerType.DEFAULT);
        }

        if (obj instanceof String || IgnoreClassUtil.inIgnoreClass(obj.getClass())) {
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
