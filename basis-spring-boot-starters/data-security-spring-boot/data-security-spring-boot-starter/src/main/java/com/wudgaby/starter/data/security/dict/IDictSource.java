package com.wudgaby.starter.data.security.dict;

import com.wudgaby.starter.data.security.dict.annotation.DictBindField;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/26 0026 14:58
 * @desc :  字典源
 */
public interface IDictSource {
    /**
     * 通过字典code返回名称
     * @param dictBindField
     * @param code
     * @return
     */
    Object getNameByCode(DictBindField dictBindField, Object code);
}
