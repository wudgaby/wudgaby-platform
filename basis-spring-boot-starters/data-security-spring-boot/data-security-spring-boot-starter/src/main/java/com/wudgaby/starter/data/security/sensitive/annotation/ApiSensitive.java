package com.wudgaby.starter.data.security.sensitive.annotation;

import java.lang.annotation.*;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2023.07.04
 * @Desc : 接口数据解密
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface ApiSensitive {
}
