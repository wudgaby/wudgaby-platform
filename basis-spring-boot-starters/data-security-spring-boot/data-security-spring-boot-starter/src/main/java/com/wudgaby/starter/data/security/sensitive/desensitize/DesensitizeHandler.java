package com.wudgaby.starter.data.security.sensitive.desensitize;

/**
 * 脱敏处理类
 * @author ;
 */
public interface DesensitizeHandler {
    /**
     * 获取脱敏的类型枚举
     * @return ;
     */
    String getSensitiveType();

    /**
     * 对数据的值进行脱敏处理
     * @param src src
     * @return 脱敏后的数据
     */
    String handle(Object src);
}
