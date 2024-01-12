package com.wudgaby.starter.tenant.util;

import cn.hutool.core.util.NumberUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 多租户 Util
 *
 * @author 芋道源码
 */
public class WebTenantUtils {
    public static final String HEADER_TENANT_ID = "tenant-id";

    public static Long getTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(HEADER_TENANT_ID);
        return NumberUtil.isNumber(tenantId) ? Long.valueOf(tenantId) : null;
    }

}
