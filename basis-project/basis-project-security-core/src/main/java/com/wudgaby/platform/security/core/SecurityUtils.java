package com.wudgaby.platform.security.core;

import cn.hutool.core.convert.Convert;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Sets;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/7 17:31
 * @Desc :   
 */
@Slf4j
@UtilityClass
public class SecurityUtils {
    private static final TransmittableThreadLocal<Map<String, Object>> TTL = new TransmittableThreadLocal<>();

    private static final String SECURITY_CONTEXT_ATTRIBUTES = "CONTEXT_AUTH_USER";
    public static final String TENANT_KEY = "tenantId";
    public static final String USER_KEY = "userId";
    public static final String DEPT_KEY = "deptId";
    public static final String CLIENT_KEY = "clientId";
    public static final String TENANT_ADMIN_KEY = "isTenantAdmin";

    private static void initMap() {
        if(TTL.get() == null){
            Map<String, Object> map = new HashMap<>(8);
            TTL.set(map);
        }
    }

    public static void set(String key, Object value) {
        initMap();
        TTL.get().put(key, value);
    }

    public static Object get(String key){
        return Optional.ofNullable(TTL.get()).map(map -> map.get(key)).orElse(null);
    }

    public static void del(String key){
        TTL.get().remove(key);
    }

    public static <T> T get(String key, Class<T> clazz){
        return Optional.ofNullable(TTL.get())
                .map(map -> Convert.convert(clazz, map.get(key)))
                .orElse(null);
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

    public static void saveExtra(Map<String, Object> extraMap) {
        initMap();
        TTL.get().putAll(extraMap);
    }

    public static void clear(){
        TTL.remove();
    }

    /**
     * 是否为超级管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isSuperAdmin(Long userId) {
        return SecurityConst.SUPER_ADMIN_ID.equals(userId);
    }

    public static boolean isSuperAdmin() {
        return isSuperAdmin((Long) getOptionalUser().map(UserInfo::getId).orElse(-1L));
    }

    public static boolean isSuperAdmin(String[] userRoles) {
        if(ArrayUtils.isEmpty(userRoles)){
            return false;
        }

        return CollectionUtils.containsAny(Arrays.asList(SecurityConst.ADMIN_ROLE_CODE_LIST), Arrays.asList(userRoles));
    }

    /**
     * 是否为管理员
     */
    public static boolean isTenantAdmin() {
        return isTenantAdmin((Set<String>) getOptionalUser().map(UserInfo::getRoleCodes).orElse(Sets.newHashSet()));
    }

    public static boolean isTenantAdmin(Set<String> rolePermission) {
        return rolePermission.contains(SecurityConst.TENANT_ADMIN_ROLE_KEY);
    }
}
