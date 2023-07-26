package com.wudgaby.starter.data.security.sensitive.desensitize;

/**
 * 脱敏类型
 * @author ；
 */
public interface SensitiveType {
    /**
     * 不脱敏
     */
    String NONE = "NONE";
    /**
     * 默认脱敏方式
     */
    String DEFAULT = "DEFAULT";
    /**
     * 中文名
     */
    String CHINESE_NAME = "CHINESE_NAME";
    /**
     * 身份证号
     */
    String ID_CARD = "ID_CARD";
    /**
     * 座机号
     */
    String FIXED_PHONE = "FIXED_PHONE";
    /**
     * 手机号
     */
    String MOBILE_PHONE = "MOBILE_PHONE";
    /**
     * 地址
     */
    String ADDRESS = "ADDRESS";
    /**
     * 电子邮件
     */
    String EMAIL = "EMAIL";
    /**
     * 银行卡
     */
    String BANK_CARD = "BANK_CARD";
    /**
     * 公司开户银行联号
     */
    String CNAPS_CODE = "CNAPS_CODE";
    /**
     * 支付签约协议号
     */
    String PAY_SIGN_NO = "PAY_SIGN_NO";
}
