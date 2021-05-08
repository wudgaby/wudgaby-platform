package com.wudgaby.mail.starter.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @ClassName : SimpleAuthenticator
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/1 12:46
 * @Desc :   TODO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SimpleAuthenticator extends Authenticator {
    private String username;
    private String password;

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
