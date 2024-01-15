package com.wudgaby.starter.apiversion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标示当前请求版本
 * @author
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    /**
     * 只支持语义化版本
     * 1
     * 1.1
     * 1.1.1
     * @return 版本号
     */
    String value();

}
