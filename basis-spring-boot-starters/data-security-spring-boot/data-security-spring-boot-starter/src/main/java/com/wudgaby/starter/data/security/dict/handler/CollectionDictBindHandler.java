package com.wudgaby.starter.data.security.dict.handler;

import com.wudgaby.starter.data.security.dict.annotation.DictBindField;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * * 处理 Collection 对象的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public class CollectionDictBindHandler implements DictBindHandler<Collection> {

    @Override
    public Object transform(Collection collection, DictBindField dictBindField) {
        if (!needCrypt(collection)) {
            return collection;
        }
        return collection.stream()
            .map(item -> DictHandlerFactory.getHandler(item, dictBindField).transform(item, dictBindField))
            .collect(Collectors.toList());
    }


    private boolean needCrypt(Collection collection) {
        return collection != null && collection.size() != 0;
    }
}
