package com.wudgaby.starter.data.security.sensitive.desensitize;

import org.apache.commons.lang3.StringUtils;

/**
 * 签约协议号脱敏方式
 * 19031317273364059018
 * 签约协议号脱敏格式为前6位后6位保留明文，中间脱敏
 * @author chenhaiyang
 */
public class PaySignNoDesensitizeHandler implements DesensitizeHandler {
    @Override
    public String getSensitiveType() {
        return SensitiveType.PAY_SIGN_NO;
    }

    @Override
    public String handle(Object src) {
        if(src==null){
            return null;
        }
        String agreementNo = src.toString();
        return StringUtils.left(agreementNo, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(agreementNo, 6), StringUtils.length(agreementNo), "*"), "***"));
    }
}
