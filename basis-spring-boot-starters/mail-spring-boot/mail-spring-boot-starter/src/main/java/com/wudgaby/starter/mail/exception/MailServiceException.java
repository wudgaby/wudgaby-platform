package com.wudgaby.starter.mail.exception;

import org.springframework.lang.Nullable;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/4/12/012 13:06
 * @Desc :
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
