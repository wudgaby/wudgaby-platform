package com.wudgaby.oauth2.uaa.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码异常
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg, Throwable t) {
        super(msg, t);
    }

    public CaptchaException(String msg) {
        super(msg);
    }
}