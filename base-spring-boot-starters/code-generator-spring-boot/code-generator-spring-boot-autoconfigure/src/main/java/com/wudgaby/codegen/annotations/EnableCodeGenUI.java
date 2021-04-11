package com.wudgaby.codegen.annotations;

import com.wudgaby.codegen.config.CodeGenUIConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName : EnableCodeGenUI
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/3 10:46
 * @Desc :   TODO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({CodeGenUIConfiguration.class})
public @interface EnableCodeGenUI {
}