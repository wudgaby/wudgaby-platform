package com.wudgaby.starter.resource.scan.annotation;

import com.wudgaby.starter.resource.scan.config.ResourceScanConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/29 10:43
 * @Desc :
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ResourceScanConfiguration.class})
public @interface EnableResourceScanAutoConfiguration {
}
