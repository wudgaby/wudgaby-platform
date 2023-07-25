package com.wudgaby.starter.data.security.crypt.annotation;

import java.lang.annotation.*;

/**
 * @author wudgaby
 */
@Target(value = ElementType.TYPE)
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CryptoBean {
}