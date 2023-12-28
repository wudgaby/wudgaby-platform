package com.wudgaby.platform.core.validation.standard;

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
 * @date : 2021.10.08
 * @Desc :
 */
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { StandardValidator.class })
public @interface CustomValid {
    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * 指定校验的实际处理器
     * @return
     */
    Class<? extends IValidHandler> handler();

}
