package com.wudgaby.platform.data.auth.annotation;

/**
 * @ClassName : DataAuth
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/7/7 16:06
 * @Desc :
 */

import java.lang.annotation.*;

@Documented
@Target( value = { ElementType.TYPE, ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
public @interface DataAuth {
}
