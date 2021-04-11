package com.wudgaby.apiautomatic.annotation;

import com.wudgaby.apiautomatic.config.MqConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName : EnableResourceAutoRegisterConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/29 10:43
 * @Desc :   TODO
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MqConfiguration.class})
public @interface EnableResourceRegisterAutoConfiguration {
}
