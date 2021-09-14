package com.wudgaby.platform.sso.core.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.wudgaby.platform.sso.core.vo.SsoUserVo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName : SsoSecurityUtils
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/4 18:44
 * @Desc :
 */
public class SsoSecurityUtils {
    private static final TransmittableThreadLocal<Map<String, Object>> ttl = new TransmittableThreadLocal();

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

    public static void setUserInfo(SsoUserVo ssoUserVo) {
        set(SECURITY_CONTEXT_ATTRIBUTES, ssoUserVo);
    }

    public static SsoUserVo getUserInfo() {
        return Optional.ofNullable(get(SECURITY_CONTEXT_ATTRIBUTES)).map(obj -> (SsoUserVo)obj).orElse(null);
    }

    public static void clear(){
        ttl.remove();
    }
}
