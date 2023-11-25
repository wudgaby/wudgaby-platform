package com.wudgaby.platform.simplesecurity.annotations;

import java.lang.annotation.*;

/**
 *  用于标记匿名访问方法
 * @author
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AnonymousAccess {
}
