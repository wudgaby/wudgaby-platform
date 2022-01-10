package com.wudgaby.platform.simplesecurity;

/**
 * @ClassName : SecurityUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/29 9:48
 * @Desc :   
 */

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName : SecurityUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/7 17:31
 * @Desc :   
 */
@Slf4j
@UtilityClass
public class SecurityUtils {
    private static TransmittableThreadLocal<Map<String, Object>> ttl = new TransmittableThreadLocal();

    private static final String SECURITY_CONTEXT_ATTRIBUTES = "CONTEXT_AUTH_USER";

    private static void initMap() {
        if(ttl.get() == null){
            Map<String, Object> map = new HashMap<>(8);
            ttl.set(map);
        }
    }

    private static void set(String key, Object value) {
        initMap();
        ttl.get().put(key, value);
    }

    private static Object get(String key){
        return Optional.ofNullable(ttl.get()).map(map -> map.get(key)).orElse(null);
    }

    public static void setCurrentUser(LoginUser loginUser) {
        set(SECURITY_CONTEXT_ATTRIBUTES, loginUser);
    }

    public static LoginUser getCurrentUser() {
        return Optional.ofNullable(get(SECURITY_CONTEXT_ATTRIBUTES)).map(obj -> (LoginUser)obj).orElse(null);
    }

    public static Optional<LoginUser> getOptionalUser() {
        return Optional.ofNullable((LoginUser)get(SECURITY_CONTEXT_ATTRIBUTES));
    }

    public static void clear(){
        ttl.remove();
    }
}
