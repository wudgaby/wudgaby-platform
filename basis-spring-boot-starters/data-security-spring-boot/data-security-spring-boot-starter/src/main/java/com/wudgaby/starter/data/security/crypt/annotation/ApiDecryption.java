package com.wudgaby.starter.data.security.crypt.annotation;

import java.lang.annotation.*;

/**
 * @ClassName : ApiDecryption
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2023.07.04
 * @Desc : 接口数据解密
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface ApiDecryption {
}
