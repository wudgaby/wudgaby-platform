package com.wudgaby.starter.captcha.interceptor;

import java.lang.annotation.*;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/12 0012 9:56
 * @desc :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CaptChaCheck {
}
