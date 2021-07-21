package com.wudgaby.platform.message.exception;

import org.springframework.lang.Nullable;

/**
 * @ClassName : JPushServiceException
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/4/12/012 13:06
 * @Desc :
 */
public class JPushServiceException extends RuntimeException {
    public JPushServiceException() {

    }

    public JPushServiceException(String message) {
        super(message);
    }

    public JPushServiceException(@Nullable String message, @Nullable Throwable t) {
        super(message, t);
    }

}
