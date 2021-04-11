package com.wudgaby.sign.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName : Signature
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/17 0:37
 * @Desc :   TODO
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Signature {
    /**
     * 按照order值排序
     */
    String ORDER_SORT = "ORDER_SORT";
    /**
     * 字典序排序
     */
    String ALPHA_SORT = "ALPHA_SORT";

    //允许重复请求
    boolean resubmit() default true;

    String sort() default Signature.ALPHA_SORT;
}
