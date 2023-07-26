package com.wudgaby.starter.data.security.exception;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/14 0014 12:16
 * @desc :
 */
public class DictTransformException extends RuntimeException{
    public DictTransformException(){
        super();
    }

    public DictTransformException(String message){
        super(message);
    }

    public DictTransformException(Throwable t){
        super(t);
    }
}
