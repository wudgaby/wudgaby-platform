package com.wudgaby.platform.core.validation.constraints.conts;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/5/29/029 11:16
 * @Desc :
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = { ConstantValidator.class })
public @interface Constant {
    String value();

    String message() default "必须为特定值";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}