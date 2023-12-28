package com.wudgaby.swagger.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/11/4 15:45
 *
 * 需要swagger展示的枚举
 * 默认 支持 枚举中属性名为 value和desc
 * 如果不是，请设置valueName和descName
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerDisplayEnum {
    /**
     * 值的方法名
     * @return
     */
    String valueName() default "name";

    /**
     * 描述的方法名
     * @return
     */
    String descName() default "getDesc";

}