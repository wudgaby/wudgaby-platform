package com.wudgaby.platform.sso.core.exception;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/24 12:01
 * @Desc :   
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
