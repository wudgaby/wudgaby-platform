package com.wudgaby.apiautomatic.annotation;

import java.lang.annotation.*;

/**
 * @ClassName : EnableResourceAutoRegisterConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/29 10:43
 * @Desc :   忽略注册
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface ApiRegisterIgnore {
}
