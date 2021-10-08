package com.wudgaby.platform.core.validation.standard;

@FunctionalInterface
public interface IValidHandler<T> {

    /**
     * 校验
     * @param value
     * @param customValid
     * @return
     */
    boolean valid(T value, CustomValid customValid);
}
