package com.wudgaby.starter.data.security.sensitive.desensitize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脱敏处理注册表
 * @author wudgaby
 */
public class DesensitizeHandlerFactory {
    private static final Map<String, DesensitizeHandler> HANDLER_MAP = new ConcurrentHashMap<>();
    static {
        HANDLER_MAP.put(SensitiveType.NONE,new NoneDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.DEFAULT,new DafaultDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.CHINESE_NAME,new NameDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.ID_CARD,new IDCardDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.MOBILE_PHONE,new MobilePhoneDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.ADDRESS,new AddressDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.EMAIL,new EmailDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.BANK_CARD,new BandCardDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.FIXED_PHONE,new FixedPhoneDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.CNAPS_CODE,new CnapsDesensitizeHandler());
        HANDLER_MAP.put(SensitiveType.PAY_SIGN_NO,new PaySignNoDesensitizeHandler());
    }

    public static void put(DesensitizeHandler sensitiveTypeHandler){
        HANDLER_MAP.put(sensitiveTypeHandler.getSensitiveType(),sensitiveTypeHandler);
    }

    public static DesensitizeHandler get(String sensitiveType){
        DesensitizeHandler sensitiveTypeHandler =  HANDLER_MAP.get(sensitiveType);
        if(sensitiveTypeHandler == null){
            throw new IllegalArgumentException(sensitiveType + " 无有效的脱敏处理.");
        }
        return sensitiveTypeHandler;
    }
}
