package com.wudgaby.platform.sso.core.exception;

/**
 * @ClassName : SsoException
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/24 12:01
 * @Desc :   TODO
 */
public class SsoException extends RuntimeException{

    public SsoException(String msg) {
        super(msg);
    }

    public SsoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SsoException(Throwable cause) {
        super(cause);
    }
}
