package com.wudgaby.starter.data.security.sensitive.handler;


import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;

import java.util.Arrays;

/**
 * 处理 Array 对象的脱敏
 * @author wudgaby
 */
public class ArraySensitiveHandler implements SensitiveHandler<Object> {

    @Override
    public Object sensitive(Object object, SensitiveField sensitiveField) {
        if (object == null) {
            return null;
        }
        return Arrays.stream((Object[]) object)
            .map(item -> SensitiveHandlerFactory.getSensitiveHandler(item, sensitiveField).sensitive(item, sensitiveField))
            .toArray();
    }
}
