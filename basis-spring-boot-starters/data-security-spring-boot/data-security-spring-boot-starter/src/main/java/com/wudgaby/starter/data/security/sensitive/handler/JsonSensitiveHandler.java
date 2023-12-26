package com.wudgaby.starter.data.security.sensitive.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;
import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveJSONFieldKey;
import com.wudgaby.starter.data.security.sensitive.desensitize.DesensitizeHandlerFactory;
import lombok.extern.slf4j.Slf4j;


/**
 * json脱敏
 * @author wudgaby
 */
@Slf4j
public class JsonSensitiveHandler implements SensitiveHandler<String> {
    @Override
    public Object sensitive(String param, SensitiveField sensitiveField) {
        if (!needCrypt(param)) {
            return param;
        }

        try{
            JSONObject jsonObject = JSONUtil.parseObj(param);
            SensitiveJSONFieldKey[] keys = sensitiveField.jsonFieldKey();
            for(SensitiveJSONFieldKey jsonFieldKey : keys){
                String key = jsonFieldKey.key();
                Object oldData = jsonObject.get(key);
                if (oldData != null) {
                    String sensitiveType = jsonFieldKey.type();
                    Object newData = DesensitizeHandlerFactory.get(sensitiveType).handle(oldData);
                    jsonObject.set(key, newData);
                }
            }
            return JSONUtil.toJsonStr(jsonObject);
        }catch (Throwable e){
            log.error("脱敏json串时失败, {}",e.getMessage(), e);
            return param;
        }
    }

    private boolean needCrypt(String param) {
        return param != null && !param.isEmpty();
    }
}
