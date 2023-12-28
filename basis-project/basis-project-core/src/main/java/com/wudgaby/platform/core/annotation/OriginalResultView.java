package com.wudgaby.platform.core.annotation;

import java.lang.annotation.*;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/26/026 22:23
 * @Desc :  controller方法返回标记,
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OriginalResultView {
}
