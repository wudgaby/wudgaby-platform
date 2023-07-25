package com.wudgaby.starter.data.security.crypt.handler;

import com.google.common.collect.Maps;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import com.wudgaby.starter.data.security.enums.HandlerType;
import com.wudgaby.starter.data.security.util.CryptUtil;
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
public class CryptHandlerFactory {
    private static final Map<HandlerType, CryptHandler> HANDLER_MAP  = Maps.newHashMap();
    static {
        HANDLER_MAP.put(HandlerType.STRING, new StringCryptHandler());
        HANDLER_MAP.put(HandlerType.COLLECTION, new CollectionCryptHandler());
        HANDLER_MAP.put(HandlerType.LIST, new ListCryptHandler());
        HANDLER_MAP.put(HandlerType.ARRAY, new ArrayCryptHandler());
        HANDLER_MAP.put(HandlerType.BEAN, new BeanCryptHandler());
        HANDLER_MAP.put(HandlerType.DEFAULT, new EmptyCryptHandler());
    }

    public static void put(@NonNull HandlerType type, @NonNull CryptHandler handler) {
        HANDLER_MAP.put(type, handler);
    }
    public static CryptHandler get(HandlerType type) {
        CryptHandler cryptHandler = HANDLER_MAP.get(type);
        if(cryptHandler == null){
            throw new IllegalArgumentException(type + " 无有效的加解密处理者.");
        }
        return cryptHandler;
    }

    public static CryptHandler getCryptHandler(Object obj, CryptoField cryptoField) {
        if (obj == null || CryptUtil.inIgnoreClass(obj.getClass())) {
            return HANDLER_MAP.get(HandlerType.DEFAULT);
        }

        // 如果是map不处理
        if (obj instanceof Map) {
            return HANDLER_MAP.get(HandlerType.DEFAULT);
        }

        if (obj instanceof String && cryptoField == null) {
            return HANDLER_MAP.get(HandlerType.DEFAULT);
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
