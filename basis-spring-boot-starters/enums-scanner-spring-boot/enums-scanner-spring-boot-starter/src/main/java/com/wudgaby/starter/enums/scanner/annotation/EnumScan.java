package com.wudgaby.starter.enums.scanner.annotation;

import java.lang.annotation.*;

/**
 * @author: zhuCan
 * @date: 2019-07-25 10:34
 * @description: 标记需要被扫描的枚举码表, 以及其默认属性
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface EnumScan {

    /**
     * 默认的枚举值
     */
    String defaultCode() default "1";

    /**
     * 枚举code的方法名
     * @return
     */
    String bindCode() default "code";

    /**
     * 枚举名称的方法名
     * @return
     */
    String bindName() default "name";

}
