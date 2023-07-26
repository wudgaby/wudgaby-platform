package com.wudgaby.starter.data.security.sensitive.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wudgaby.starter.data.security.sensitive.SensitiveSerializer;
import com.wudgaby.starter.data.security.sensitive.desensitize.SensitiveType;

import java.lang.annotation.*;


/**
 *  @author ;
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerializer.class)
public @interface SensitiveFieldSerializer {
    /**
     * 脱敏类型. SensitiveType或自定义
     */
    String value() default SensitiveType.DEFAULT;

}
