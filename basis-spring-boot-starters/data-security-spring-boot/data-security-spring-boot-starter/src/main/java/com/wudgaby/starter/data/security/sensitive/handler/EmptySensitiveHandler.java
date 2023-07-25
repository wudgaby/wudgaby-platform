package com.wudgaby.starter.data.security.sensitive.handler;

import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;

/**
 * 空的脱敏执行者
 * @author wudgaby
 */
public class EmptySensitiveHandler implements SensitiveHandler<Object> {

    @Override
    public Object sensitive(Object param, SensitiveField sensitiveField) {
        return param;
    }
}
