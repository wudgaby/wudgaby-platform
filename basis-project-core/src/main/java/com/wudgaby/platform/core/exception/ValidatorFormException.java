package com.wudgaby.platform.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName : ValidatorFormException
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/24/024 2:03
 * @Desc :   TODO
 */
@EqualsAndHashCode(callSuper=false)
@Data
public class ValidatorFormException extends RuntimeException {
    private int errorCode;
    private Object data;

    public ValidatorFormException(int errorCode, String message, Object data, Throwable e){
        super(message, e);
        this.errorCode = errorCode;
        this.data = data;
    }

    public ValidatorFormException(int errorCode, String message, Object data){
        this(errorCode, message, data, null);
    }

    public ValidatorFormException(int errorCode, String message){
        this(errorCode, message, null, null);
    }

    public ValidatorFormException(String message, Throwable e){
        this(0, message, null, e);
    }

    public ValidatorFormException(String message, Object data){
        this(0, message, data, null);
    }

    public ValidatorFormException(Throwable e){
        super(e);
    }

    public ValidatorFormException(){}

}
