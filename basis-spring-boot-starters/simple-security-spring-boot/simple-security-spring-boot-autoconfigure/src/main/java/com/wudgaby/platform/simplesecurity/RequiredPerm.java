package com.wudgaby.platform.simplesecurity;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiredPerm {
    String[] roles() default {};
    String[] perms() default {};
}
