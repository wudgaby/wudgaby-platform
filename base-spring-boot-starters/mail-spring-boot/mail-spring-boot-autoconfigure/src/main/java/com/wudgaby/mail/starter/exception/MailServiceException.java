package com.wudgaby.mail.starter.exception;

import org.springframework.lang.Nullable;

/**
 * @ClassName : MailServiceException
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/4/12/012 13:06
 * @Desc :   TODO
 */
public class MailServiceException extends RuntimeException{
    public MailServiceException(){

    }

    public MailServiceException(String message){
        super(message);
    }

    public MailServiceException(@Nullable String message, @Nullable Throwable t){
        super(message, t);
    }

}
