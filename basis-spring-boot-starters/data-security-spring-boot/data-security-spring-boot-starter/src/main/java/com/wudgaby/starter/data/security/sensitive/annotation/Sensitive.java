package com.wudgaby.starter.data.security.sensitive.annotation;

import java.lang.annotation.*;

/**
 * 标记在DTO类上，用于说明是否支持加解密
 * @author ;
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Sensitive {
    /**
     * 是否开启加解密和脱敏模式
     * @return Sensitive
     */
    boolean value() default true;
}
