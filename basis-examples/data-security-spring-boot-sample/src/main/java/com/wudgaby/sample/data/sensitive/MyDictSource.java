package com.wudgaby.sample.data.sensitive;

import com.google.common.collect.Maps;
import com.wudgaby.sample.data.sensitive.enums.StatusEnum;
import com.wudgaby.starter.data.security.dict.IDictSource;
import com.wudgaby.starter.data.security.dict.annotation.DictBindField;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/26 0026 17:46
 * @desc :
 */
@Component
public class MyDictSource implements IDictSource {
    private final static Map<Object, String> sexMap = Maps.newHashMap();
    static {
        sexMap.put("0", "男");
        sexMap.put("1", "女");
    }

    @Override
    public Object getNameByCode(DictBindField dictBindField, Object code) {
        if(dictBindField.type().equals("sex")){
            return sexMap.get(code);
        }

        if(dictBindField.type().equals("status")){
            return StatusEnum.get((Integer)code).getName();
        }

        return "";

    }
}
