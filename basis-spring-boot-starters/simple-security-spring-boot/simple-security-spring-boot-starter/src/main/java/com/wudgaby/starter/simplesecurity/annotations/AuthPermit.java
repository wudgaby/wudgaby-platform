package com.wudgaby.starter.simplesecurity.annotations;

import com.wudgaby.starter.simplesecurity.LogicType;

import java.lang.annotation.*;


/**
 * @author:
 * roles 和 perms 关系.
 * 当roles, perms未设置时. 是否开启请求地址鉴权
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AuthPermit {
    /**
     * 支持spel表达式
     */
    String value() default "";
    /**
     * 所需角色标识. 优先级:1
     */
    String[] roles() default {};

    /**
     * 所需权限标识. 优先级:2
     */
    String[] perms() default {};

    /**
     * 逻辑类型
     */
    LogicType logicType() default LogicType.OR;

    /**
     * 是否启用url鉴权
     */
    boolean enableUrl() default false;
}
