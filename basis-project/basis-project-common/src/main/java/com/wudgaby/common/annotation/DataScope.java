package com.wudgaby.common.annotation;

import java.lang.annotation.*;

/**
 * @ClassName : DataScope
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/9 18:32
 * @Desc :   TODO
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * 部门表的别名
     * @return
     */
    String deptAlias() default "";

    /**
     * 用户表的别名
     * @return
     */
    String userAlias() default "";
}
