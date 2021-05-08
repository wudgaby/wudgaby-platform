package com.wudgaby.platform.core.support;

import org.springframework.core.convert.converter.Converter;

import java.util.Objects;

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
        if(source != null){
            return source.trim();
        }
        return source;
    }
}
