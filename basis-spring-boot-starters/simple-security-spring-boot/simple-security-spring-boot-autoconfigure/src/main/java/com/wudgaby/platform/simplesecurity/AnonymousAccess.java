package com.wudgaby.platform.simplesecurity;

import java.lang.annotation.*;

/**
 *  用于标记匿名访问方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AnonymousAccess {
}
