package com.wudgaby.starter.data.security.sensitive.annotation;

import com.wudgaby.starter.data.security.sensitive.config.SensitiveConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * @author wudgaby
 * 使用spring.factories自动注入
 */
@Deprecated
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SensitiveConfig.class)
public @interface EnableDataSensitive {

}
