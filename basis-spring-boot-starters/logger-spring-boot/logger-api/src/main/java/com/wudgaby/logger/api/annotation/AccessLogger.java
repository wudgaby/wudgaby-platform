package com.wudgaby.logger.api.annotation;

import java.lang.annotation.*;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020.06.04
 * @Desc :   访问日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLogger {
    /**
     * 简单描述
     * @return
     */
    String value() default "";

    /**
     * 详细描述
     * @return
     */
    String[] describe() default "";

    /**
     * 忽略
     * @return
     */
    boolean ignore() default false;
}
