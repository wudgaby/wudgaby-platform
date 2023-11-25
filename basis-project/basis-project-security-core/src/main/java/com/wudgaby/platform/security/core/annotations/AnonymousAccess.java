package com.wudgaby.platform.security.core.annotations;

import java.lang.annotation.*;

/**
 *  用于标记匿名访问方法
 * @author wudgaby
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AnonymousAccess {
}
