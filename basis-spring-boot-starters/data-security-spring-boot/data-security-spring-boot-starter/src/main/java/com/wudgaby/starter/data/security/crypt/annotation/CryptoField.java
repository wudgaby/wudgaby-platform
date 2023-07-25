package com.wudgaby.starter.data.security.crypt.annotation;

import java.lang.annotation.*;

/**
 * @author wudgaby
 */
@Target(value = ElementType.FIELD)
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CryptoField {
    /**
     * 是否加密
     * @return
     */
    boolean encrypt() default true;

    /**
     * 是否机密
     * @return
     */
    boolean decrypt() default true;
}