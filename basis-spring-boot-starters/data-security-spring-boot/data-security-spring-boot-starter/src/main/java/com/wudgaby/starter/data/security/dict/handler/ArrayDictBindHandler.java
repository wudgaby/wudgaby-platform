package com.wudgaby.starter.data.security.dict.handler;

import com.wudgaby.starter.data.security.dict.annotation.DictBindField;

import java.util.Arrays;

/**
 * 处理 Array 对象的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public class ArrayDictBindHandler implements DictBindHandler<Object> {

    @Override
    public Object transform(Object object, DictBindField dictBindField) {
        if (object == null) {
            return null;
        }
        return Arrays.stream((Object[]) object)
            .map(item -> DictHandlerFactory.getHandler(item, dictBindField).transform(item, dictBindField))
            .toArray();
    }

}
