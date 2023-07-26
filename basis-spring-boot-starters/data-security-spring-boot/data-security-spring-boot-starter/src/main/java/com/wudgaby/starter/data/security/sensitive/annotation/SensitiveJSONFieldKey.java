package com.wudgaby.starter.data.security.sensitive.annotation;

import java.lang.annotation.*;

/**
 * json字段中需要脱敏的key字段以及key脱敏类型
 * @author chenhaiyang
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SensitiveJSONFieldKey {
    /**
     * json中的key的类型
     */
    String key();
    /**
     * 脱敏类型. SensitiveType或自定义
     */
    String type() default "DEFAULT";
}
