package com.wudgaby.starter.mail.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/4/1 12:46
 * @Desc :
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
