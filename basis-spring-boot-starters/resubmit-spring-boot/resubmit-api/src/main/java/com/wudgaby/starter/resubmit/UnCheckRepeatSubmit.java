package com.wudgaby.starter.resubmit;

import java.lang.annotation.*;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/11/14 18:22
 * @Desc :   
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnCheckRepeatSubmit {

}
