package com.wudgaby.starter.data.security.sensitive.handler;

import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;

/**
 * 脱敏处理抽象类
 * @author wudgaby
 */
public interface SensitiveHandler<T> {
    /**
     * 脱敏
     * @param param
     * @param sensitiveField
     * @return
     */
    Object sensitive(T param, SensitiveField sensitiveField);
}
