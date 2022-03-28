package com.wudgaby.platform.core.validation.constraints.datetime;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @ClassName : DateTimeValue
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/15 1:38
 * @Desc :   校验日期格式
 */
@Documented
@Constraint(validatedBy = DateTimeValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeValue {

    String message() default "日期格式不正确, 正确格式应为yyyy-MM-dd HH:mm:ss";

    String format() default "yyyy-MM-dd HH:mm:ss";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        DateTimeValue[] value();
    }
}
