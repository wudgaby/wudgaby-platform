package com.wudgaby.starter.crypto.annotation;

import java.lang.annotation.*;

/**
 * @ClassName : ApiEncryption
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2023.07.04
 * @Desc : 接口数据加密
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiEncryption {
}
