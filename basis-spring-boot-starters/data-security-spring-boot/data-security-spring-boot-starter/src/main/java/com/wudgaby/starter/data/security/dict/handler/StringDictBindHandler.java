package com.wudgaby.starter.data.security.dict.handler;

import com.wudgaby.starter.data.security.dict.IDictSource;
import com.wudgaby.starter.data.security.dict.annotation.DictBindField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 处理 String 对象的加解密
 *
 * @author junliang.zhuo
 * @date 2019-03-29 11:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringDictBindHandler implements DictBindHandler<Object> {
    private IDictSource dictSource;

    @Override
    public Object transform(Object param, DictBindField dictBindField) {
        if (needCrypt(param)) {
            return dictSource.getNameByCode(dictBindField, param);
        }
        return param;
    }

    private boolean needCrypt(Object param) {
        return dictSource !=null && param != null ;
    }
}

