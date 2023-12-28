package com.wudgaby.sign.api;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/17 1:03
 * @Desc :
 */
public class SignatureException extends RuntimeException {
    public SignatureException(){
    }

    public SignatureException(String message){
        super(message);
    }

    public SignatureException(String message, Throwable throwable){
        super(message, throwable);
    }
}
