package com.wudgaby.starter.data.security.dict.handler;

import com.wudgaby.starter.data.security.dict.annotation.DictBindField;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理 List 对象的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public class ListDictBindHandler implements DictBindHandler<List> {

    @Override
    public Object transform(List param, DictBindField dictBindField) {
        if (!needCrypt(param)) {
            return param;
        }
        return param.stream()
            .map(item -> DictHandlerFactory.getHandler(item, dictBindField).transform(item, dictBindField))
            .collect(Collectors.toList());
    }

    private boolean needCrypt(List list) {
        return list != null && list.size() != 0;
    }
}
