package com.wudgaby.starter.tenant.exception;

/**
 * 租户异常类
 *
 * @author Lion Li
 */
public class TenantException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TenantException() {
        super("租户异常");
    }
    public TenantException(String message) {
        super(message);
    }

    public TenantException(String message, Throwable cause) {
        super(message, cause);
    }
}
