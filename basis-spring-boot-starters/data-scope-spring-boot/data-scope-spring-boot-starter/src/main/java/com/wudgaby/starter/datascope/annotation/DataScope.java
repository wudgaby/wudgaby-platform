package com.wudgaby.starter.datascope.annotation;

import java.lang.annotation.*;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/2 0002 10:51
 * @desc :
 */
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {

}
