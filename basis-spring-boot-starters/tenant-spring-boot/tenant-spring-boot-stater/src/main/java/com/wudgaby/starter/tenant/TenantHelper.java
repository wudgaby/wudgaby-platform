package com.wudgaby.starter.tenant;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.wudgaby.platform.security.core.SecurityUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * 租户助手
 *
 * @author Lion Li
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TenantHelper {

    private static final String DYNAMIC_TENANT_KEY = "dynamicTenant";

    /**
     * 租户功能是否启用
     */
    public static boolean isEnable() {
        return Convert.toBool(SpringUtil.getProperty("tenant.enable"), false);
    }

    /**
     * 开启忽略租户(开启后需手动调用 {@link #disableIgnore()} 关闭)
     */
    public static void enableIgnore() {
        InterceptorIgnoreHelper.handle(IgnoreStrategy.builder().tenantLine(true).build());
    }

    /**
     * 关闭忽略租户
     */
    public static void disableIgnore() {
        InterceptorIgnoreHelper.clearIgnoreStrategy();
    }

    /**
     * 在忽略租户中执行
     *
     * @param handle 处理执行方法
     */
    public static void ignore(Runnable handle) {
        enableIgnore();
        try {
            handle.run();
        } finally {
            disableIgnore();
        }
    }

    /**
     * 在忽略租户中执行
     *
     * @param handle 处理执行方法
     */
    public static <T> T ignore(Supplier<T> handle) {
        enableIgnore();
        try {
            return handle.get();
        } finally {
            disableIgnore();
        }
    }

    /**
     * 设置动态租户(一直有效 需要手动清理)
     * <p>
     * 如果为未登录状态下 那么只在当前线程内生效
     */
    public static void setDynamic(String tenantId) {
        if (!isEnable()) {
            return;
        }
        String cacheKey = DYNAMIC_TENANT_KEY + ":" + SecurityUtils.get(SecurityUtils.USER_KEY);
        SecurityUtils.set(cacheKey, tenantId);
    }

    /**
     * 获取动态租户(一直有效 需要手动清理)
     * <p>
     * 如果为未登录状态下 那么只在当前线程内生效
     */
    public static String getDynamic() {
        if (!isEnable()) {
            return null;
        }
        String tenantId = SecurityUtils.get(SecurityUtils.TENANT_KEY, String.class);
        if (StringUtils.isNotBlank(tenantId)) {
            return tenantId;
        }
        return tenantId;
    }

    /**
     * 清除动态租户
     */
    public static void clearDynamic() {
        if (!isEnable()) {
            return;
        }
        String cacheKey = DYNAMIC_TENANT_KEY + ":" + SecurityUtils.get(SecurityUtils.USER_KEY);
        SecurityUtils.del(cacheKey);
    }

    /**
     * 在动态租户中执行
     *
     * @param handle 处理执行方法
     */
    public static void dynamic(String tenantId, Runnable handle) {
        setDynamic(tenantId);
        try {
            handle.run();
        } finally {
            clearDynamic();
        }
    }

    /**
     * 在动态租户中执行
     *
     * @param handle 处理执行方法
     */
    public static <T> T dynamic(String tenantId, Supplier<T> handle) {
        setDynamic(tenantId);
        try {
            return handle.get();
        } finally {
            clearDynamic();
        }
    }

    /**
     * 获取当前租户id(动态租户优先)
     */
    public static String getTenantId() {
        if (!isEnable()) {
            return null;
        }
        String tenantId = TenantHelper.getDynamic();
        if (StringUtils.isBlank(tenantId)) {
            tenantId = SecurityUtils.get(SecurityUtils.TENANT_KEY, String.class);
        }
        return tenantId;
    }
}
