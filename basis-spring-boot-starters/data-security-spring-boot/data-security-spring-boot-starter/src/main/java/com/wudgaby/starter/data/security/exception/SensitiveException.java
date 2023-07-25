package com.wudgaby.starter.data.security.exception;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/14 0014 12:16
 * @desc :
 */
public class SensitiveException extends RuntimeException{
    public SensitiveException(Throwable t){
        super(t);
    }
}
