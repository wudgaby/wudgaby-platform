package com.wudgaby.starter.data.security.crypt.annotation;

import com.wudgaby.starter.data.security.crypt.config.CryptoConfig;
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
@Import({CryptoConfig.class})
public @interface EnableDataCrypto {

}
