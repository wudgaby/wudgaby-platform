package com.wudgaby.platform.openapi.annotations;

import java.lang.annotation.*;

/**
 * @ClassName : EnableAccessToken
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/1 17:23
 * @Desc :
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessTokenVerifier {

}