package com.wudgaby.oauth2.uaa.constants;

/**
 * @ClassName : GrantType
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/24 18:15
 * @Desc :   TODO
 */
public interface GrantType {
    String AUTH_CODE = "authorization_code";
    String PASSWORD = "password";
    String CLIENT_CRED = "client_credentials";
    String IMPLICIT = "implicit";
    String REFRESH_TOKEN = "refresh_token";
}
