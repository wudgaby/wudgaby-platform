package com.wudgaby.platform.webcore.support;

import org.springframework.core.convert.converter.Converter;

/**
 * @ClassName : CustomStringTrimConverter
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/18 13:21
 * @Desc :   去掉前后空格
 */
public class CustomStringTrimConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        return source.trim();
    }
}
