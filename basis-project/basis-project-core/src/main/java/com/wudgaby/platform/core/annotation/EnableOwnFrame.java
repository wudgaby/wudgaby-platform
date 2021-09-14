package com.wudgaby.platform.core.annotation;

import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
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
@ComponentScan(value = EnableOwnFrame.BASE_PACKAGE,
        excludeFilters = { @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                            @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface EnableOwnFrame {
    String BASE_PACKAGE = "com.wudgaby.platform";

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
    String[] scanBasePackages() default { BASE_PACKAGE };

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses")
    Class<?>[] scanBasePackageClasses() default {};
}
