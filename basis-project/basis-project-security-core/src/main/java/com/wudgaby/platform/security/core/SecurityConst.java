package com.wudgaby.platform.security.core;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2022/1/10 0010 14:40
 * @Desc :
 */
public interface SecurityConst {
    String SESSION_LOGGED_USER = "session_logged_user";
    String[] ADMIN_ROLE_CODE_LIST = new String[]{"superAdmin", "admin"};

    Long SUPER_ADMIN_ID = 1L;

    String TENANT_ADMIN_ROLE_KEY = "tenantAdmin";
}
