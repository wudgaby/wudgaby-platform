package com.wudgaby.sign.api;

import java.lang.annotation.*;

/**
 * @ClassName : SignatureField
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/19 14:43
 * @Desc :
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignatureField {
    //签名顺序
    int order() default 0;

    //字段name自定义值
    String customName() default "";

    //字段value自定义值
    String customValue() default "";
}
