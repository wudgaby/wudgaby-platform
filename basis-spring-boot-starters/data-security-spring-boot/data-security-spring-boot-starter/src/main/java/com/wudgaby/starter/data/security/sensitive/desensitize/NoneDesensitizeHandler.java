package com.wudgaby.starter.data.security.sensitive.desensitize;

/**
 * 不脱敏
 * @author chenhaiyang
 */
public class NoneDesensitizeHandler implements DesensitizeHandler {
    @Override
    public SensitiveType getSensitiveType() {
        return SensitiveType.NONE;
    }

    @Override
    public String handle(Object src) {
        if(src!=null){
            return src.toString();
        }
        return null;
    }
}
