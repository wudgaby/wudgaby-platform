package com.wudgaby.starter.data.security.dict.annotation;

import java.lang.annotation.*;

/**
 * @ClassName : ApiDecryption
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2023.07.04
 * @Desc : 接口数据解密
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DictBindField {
    /**
     * 字典类型
     * @return
     */
    String type();

    /**
     * 目标字段填充
     * @return
     */
    String target();
}
