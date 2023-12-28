package com.wudgaby.oauth2.uaa.constants;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/24 18:15
 * @Desc :
 */
public interface GrantType {
    String AUTH_CODE = "authorization_code";
    String PASSWORD = "password";
    String CLIENT_CRED = "client_credentials";
    String IMPLICIT = "implicit";
    String REFRESH_TOKEN = "refresh_token";
}
