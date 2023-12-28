package com.wudgaby.platform.core.validation.standard;

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
public @interface CustomValids {
    CustomValid[] value();
}
