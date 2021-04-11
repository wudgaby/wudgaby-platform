package com.wudgaby.platform.core.validation;

import com.wudgaby.platform.core.validation.constraints.PhoneAndTelephoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @ClassName : Telephone
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/12/6 14:14
 * @Desc :   固定电话
 */
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { PhoneAndTelephoneValidator.class })
public @interface PhoneAndTelephone {
    String message() default "号码格式不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}