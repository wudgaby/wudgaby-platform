package com.wudgaby.sign.api;

/**
 * @ClassName : SignatureException
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/17 1:03
 * @Desc :   TODO
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
