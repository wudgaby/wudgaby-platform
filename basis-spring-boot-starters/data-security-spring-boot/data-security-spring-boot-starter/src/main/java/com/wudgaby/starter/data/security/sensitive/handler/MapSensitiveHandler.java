package com.wudgaby.starter.data.security.sensitive.handler;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveJSONFieldKey;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * map脱敏
 * @author wudgaby
 */
public class MapSensitiveHandler implements SensitiveHandler<Map<String, Object>> {

    @Override
    public Object sensitive(Map<String, Object> map, SensitiveField sensitiveField) {
        if (!needCrypt(map)) {
            return map;
        }

        return map.entrySet().stream()
                .filter(item ->  filter(item, sensitiveField))
                .map(item -> SensitiveHandlerFactory.getSensitiveHandler(item, sensitiveField).sensitive(item, sensitiveField))
                .collect(Collectors.toList());
    }

    private boolean filter(Map.Entry<String, Object> item, SensitiveField sensitiveField){
        SensitiveJSONFieldKey[] fieldKeys = sensitiveField.jsonFieldKey();
        if(ObjectUtil.isNotNull(item) || ArrayUtil.isEmpty(fieldKeys)){
            return false;
        }
        return Arrays.stream(fieldKeys).filter(key -> StrUtil.equals(item.getKey(), key.key())).count() > 0;
    }

    private boolean needCrypt(Map map) {
        return map != null && map.size() != 0;
    }
}
