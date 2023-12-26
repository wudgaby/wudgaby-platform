package com.wudgaby.starter.data.security.sensitive.handler;

import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理 List 对象的脱敏
 * @author wudgaby
 */
public class ListSensitiveHandler implements SensitiveHandler<List> {

    @Override
    public Object sensitive(List list, SensitiveField sensitiveField) {
        if (!needCrypt(list)) {
            return list;
        }
        return list.stream()
            .map(item -> SensitiveHandlerFactory.getSensitiveHandler(item, sensitiveField).sensitive(item, sensitiveField))
            .collect(Collectors.toList());
    }

    private boolean needCrypt(List list) {
        return list != null && !list.isEmpty();
    }
}
