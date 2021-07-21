package com.wudgaby.platform.httpclient.support;

import java.net.PasswordAuthentication;

/**
 * @ClassName : ProxyAuthenticator
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/9 9:26
 * @Desc :
 */
public class ProxyAuthenticator extends java.net.Authenticator {
    private String username;
    private String password;

    public ProxyAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }
}
