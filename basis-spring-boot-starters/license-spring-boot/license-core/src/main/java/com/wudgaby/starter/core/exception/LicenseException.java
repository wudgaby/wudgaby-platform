package com.wudgaby.starter.core.exception;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 11:16
 * @desc :
 */
public class LicenseException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public LicenseException() {
        super("无效License");
    }
    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
