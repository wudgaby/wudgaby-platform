package com.wudgaby.platform.simplesecurity;

import java.lang.annotation.*;


/**
 * @author:
 * roles 和 perms 或关系.
 * 满足其中一个条件就可以
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AuthPermit {
    /**
     * 角色标识
     */
    String[] roles() default {};

    /**
     * 权限标识
     * @return
     */
    String[] perms() default {};
}
