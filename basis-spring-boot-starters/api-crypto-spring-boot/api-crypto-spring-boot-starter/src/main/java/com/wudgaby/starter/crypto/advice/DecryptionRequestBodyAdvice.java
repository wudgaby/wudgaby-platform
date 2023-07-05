package com.wudgaby.starter.crypto.advice;

import com.wudgaby.starter.crypto.annotation.ApiDecryption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/4 0004 18:00
 * @desc :
 */
@Slf4j
@RestControllerAdvice
public class DecryptionRequestBodyAdvice extends RequestBodyAdviceAdapter {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(ApiDecryption.class) || methodParameter.hasParameterAnnotation(ApiDecryption.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return new DecodeInputMessage(inputMessage);
    }
}
