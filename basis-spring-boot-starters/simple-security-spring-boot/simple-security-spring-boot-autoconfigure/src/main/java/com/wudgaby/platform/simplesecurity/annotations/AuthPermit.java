package com.wudgaby.platform.simplesecurity.annotations;

import com.wudgaby.platform.simplesecurity.LogicType;

import java.lang.annotation.*;


/**
 * @author:
 * roles 和 perms 关系.
 * 当roles, perms未设置时. 根据请求地址鉴权
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AuthPermit {
    /**
     * 所需角色标识. 优先级:1
     */
    String[] roles() default {};

    /**
     * 所需权限标识. 优先级:2
     * @return
     */
    String[] perms() default {};

    /**
     * 逻辑类型
     */
    LogicType logicType() default LogicType.OR;
}
