package com.wudgaby.platform.utils.vo;

import java.lang.annotation.*;

/**
 * @ClassName : ComparisonInclude
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/27 11:07
 * @Desc :   TODO
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComparisonFiledInclude {
    String value();

    boolean ignoreContent() default false;

    String format() default "";

    String desc() default "";

    //Class<?> enumClass() default Void.class;
}
