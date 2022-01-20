package com.wudgaby.platform.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/20 0020 12:24
 * @Desc :
 */
public class RequestContext {
    private static TransmittableThreadLocal<Map<String, Object>> ttl = new TransmittableThreadLocal();

    public static final String REQ_PATH = "reqPath";
    public static final String REQ_ID = "reqId";

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
}
