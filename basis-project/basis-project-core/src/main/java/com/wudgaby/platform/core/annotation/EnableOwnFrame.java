package com.wudgaby.platform.core.annotation;

import com.wudgaby.platform.core.config.ApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @ClassName : EnableOwnFrame
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 15:31
 * @Desc :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ApplicationConfiguration.class)
public @interface EnableOwnFrame {
    String BASE_PACKAGE = "com.wudgaby";

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
    String[] scanBasePackages() default { BASE_PACKAGE };

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses")
    Class<?>[] scanBasePackageClasses() default {};
}
