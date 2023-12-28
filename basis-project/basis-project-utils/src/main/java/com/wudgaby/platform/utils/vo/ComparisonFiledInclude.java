package com.wudgaby.platform.utils.vo;

import java.lang.annotation.*;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/27 11:07
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
