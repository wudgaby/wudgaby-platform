package com.wudgaby.starter.access.limit;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/2 0002 10:43
 * @desc :  访问限制异常
 */
public class AccessLimitException extends RuntimeException {
    public AccessLimitException(){
        super("访问太快");
    }

    public AccessLimitException(String message){
        super(message);
    }
}
