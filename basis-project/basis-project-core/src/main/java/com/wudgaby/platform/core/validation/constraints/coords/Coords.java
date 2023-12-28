package com.wudgaby.platform.core.validation.constraints.coords;

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
 * @date : 2020/6/15 1:38
 * @Desc :   经纬度
 */
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
//指定验证器
@Constraint(validatedBy = CoordsValidator.class)
@Documented
public @interface Coords {
    String message() default "{coords.validate.invalid}";
    //分组
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
