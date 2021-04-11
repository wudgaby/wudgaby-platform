package com.wudgaby.platform.sso.core.constant;

/**
 * @ClassName : SsoConst
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 17:47
 * @Desc :   TODO
 */
public interface SsoConst {
    String SSO_LOGIN_URL = "/sso/login";

    String SSO_LOGOUT_URL = "/sso/logout";

    String SSO_CHECK_URL = "/sso/check";

    String SSO_USER_INFO_URL = "/sso/user/me";

    String SSO_USER_RESOURCE_URL = "/sso/user/resource";

    /**
     * 因用了spring session 与 server.servlet.cookie.name相同
     */
    String SSO_SESSION_ID = "SESSION";

    String SSO_HEADER_X_TOKEN = "x-auth-token";

    String REDIRECT_URL = "redirectUrl";

    String ERROR_MESSAGE = "errorMsg";

    String SSO_USER = "sso_user";

    String SSO_SERVER = "sso_server";

    String COOKIE_PATH = "/";

}
