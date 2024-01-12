package com.wudgaby.platform.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/4 1:14
 * @Desc :
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String message){
        super(message);
    }
}
