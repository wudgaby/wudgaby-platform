package com.wudgaby.starter.data.security.dict.handler;

import com.wudgaby.starter.data.security.dict.annotation.DictBindField;

/**
 * 加解密处理抽象类
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
public interface DictBindHandler<T> {
    /**
     * 加密
     * @param param
     * @param dictBindField
     * @return
     */
    Object transform(T param, DictBindField dictBindField);
}
