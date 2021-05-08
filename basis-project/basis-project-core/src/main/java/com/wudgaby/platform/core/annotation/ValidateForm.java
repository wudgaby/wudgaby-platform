package com.wudgaby.platform.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName : ValidatorForm
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/24/024 3:14
 * @Desc :   验证
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateForm {
}
