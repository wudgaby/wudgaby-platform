package com.wudgaby.plugin.resubmit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName : RepeatSubmit
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/14 18:22
 * @Desc :   
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnCheckRepeatSubmit {

}
