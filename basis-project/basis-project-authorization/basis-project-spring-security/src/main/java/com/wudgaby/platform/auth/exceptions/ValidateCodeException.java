package com.wudgaby.platform.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @ClassName : ValidateCodeException
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/4 1:14
 * @Desc :   TODO
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String message){
        super(message);
    }
}
