package com.wudgaby.platform.core.annotation;

import java.lang.annotation.*;

/**
 * @ClassName : OriginalResultView
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/26/026 22:23
 * @Desc :  controller方法返回标记,
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResultView {
}
