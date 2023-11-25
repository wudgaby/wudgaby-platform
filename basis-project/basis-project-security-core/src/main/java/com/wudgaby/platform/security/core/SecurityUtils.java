package com.wudgaby.platform.security.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/7 17:31
 * @Desc :   
 */
@Slf4j
@UtilityClass
public class SecurityUtils {
    private static final TransmittableThreadLocal<Map<String, Object>> TTL = new TransmittableThreadLocal<>();

    private static final String SECURITY_CONTEXT_ATTRIBUTES = "CONTEXT_AUTH_USER";

    private static void initMap() {
        if(TTL.get() == null){
            Map<String, Object> map = new HashMap<>(8);
            TTL.set(map);
        }
    }

    private static void set(String key, Object value) {
        initMap();
        TTL.get().put(key, value);
    }

    private static Object get(String key){
        return Optional.ofNullable(TTL.get()).map(map -> map.get(key)).orElse(null);
    }

    public static void setCurrentUser(UserInfo loginUser) {
        set(SECURITY_CONTEXT_ATTRIBUTES, loginUser);
    }

    public static UserInfo getCurrentUser() {
        return (UserInfo) get(SECURITY_CONTEXT_ATTRIBUTES);
    }

    public static Optional<UserInfo> getOptionalUser() {
        return Optional.ofNullable((UserInfo)get(SECURITY_CONTEXT_ATTRIBUTES));
    }

    public static void clear(){
        TTL.remove();
    }
}
