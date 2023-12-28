package com.wudgaby.platform.core.validation.constraints.phone;

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
 * @date : 2019/12/6 14:14
 * @Desc :   固定电话
 */
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { PhoneValidator.class })
public @interface Telephone {
    String message() default "固话格式不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}