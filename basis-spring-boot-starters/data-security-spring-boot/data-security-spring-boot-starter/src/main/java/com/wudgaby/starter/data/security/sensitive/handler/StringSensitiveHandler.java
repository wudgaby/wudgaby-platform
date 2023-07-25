package com.wudgaby.starter.data.security.sensitive.handler;

import com.wudgaby.starter.data.security.sensitive.annotation.SensitiveField;
import com.wudgaby.starter.data.security.sensitive.desensitize.DesensitizeHandlerFactory;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 处理 String 脱敏
 * @author wudgaby
 */
@Data
@NoArgsConstructor
public class StringSensitiveHandler implements SensitiveHandler<String> {

    @Override
    public Object sensitive(String param, SensitiveField sensitiveField) {
        if (needCrypt(param)) {
            return DesensitizeHandlerFactory.get(sensitiveField.value()).handle(param);
        }
        return param;
    }

    private boolean needCrypt(String param) {
        return param != null && param.length() != 0;
    }
}

