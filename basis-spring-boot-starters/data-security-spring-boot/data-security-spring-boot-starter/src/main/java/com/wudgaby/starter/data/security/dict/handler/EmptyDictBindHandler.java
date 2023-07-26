package com.wudgaby.starter.data.security.dict.handler;

import com.wudgaby.starter.data.security.dict.annotation.DictBindField;

/**
 * 空的加解密执行者，避免过多空指针判断
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public class EmptyDictBindHandler implements DictBindHandler<Object> {

    @Override
    public Object transform(Object param, DictBindField dictBindField) {
        return param;
    }
}
