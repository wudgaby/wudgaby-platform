package com.wudgaby.starter.data.security.sensitive.annotation;

import com.wudgaby.starter.data.security.sensitive.desensitize.SensitiveType;

import java.lang.annotation.*;


/**
 *  @author ;
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SensitiveField {
    /**
     * 脱敏类型. SensitiveType或自定义
     */
    String value() default SensitiveType.DEFAULT;

    /**
     * json脱敏
     * @return
     */
    SensitiveJSONFieldKey[] jsonFieldKey() default {};
}
