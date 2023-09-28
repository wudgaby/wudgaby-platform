package com.wudgaby.starter.captcha.core;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 17:22
 * @desc :
 */
public class CaptchaException extends RuntimeException{
    public CaptchaException(String msg, Throwable t) {
        super(msg, t);
    }

    public CaptchaException(String msg) {
        super(msg);
    }
}
