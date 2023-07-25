package com.wudgaby.starter.data.security.sensitive.annotation;

import java.lang.annotation.*;

/**
 * 对json内的key_value进行脱敏
 * @author chenhaiyang
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SensitiveJSONField {
    /**
     * 需要脱敏的字段的数组
     */
    SensitiveJSONFieldKey[] value();
}
