package com.wudgaby.starter.data.security.sensitive.handler;

import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * * 处理 Collection 对象的脱敏
 * @author wudgaby
 */
public class CollectionSensitiveHandler implements SensitiveHandler<Collection> {

    @Override
    public Object sensitive(Collection collection, SensitiveField sensitiveField) {
        if (!needCrypt(collection)) {
            return collection;
        }
        return collection.stream()
            .map(item -> SensitiveHandlerFactory.getSensitiveHandler(item, sensitiveField).sensitive(item, sensitiveField))
            .collect(Collectors.toList());
    }

    private boolean needCrypt(Collection collection) {
        return collection != null && collection.size() != 0;
    }
}
