package com.wudgaby.platform.httpclient.support.converter;

import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author wudgaby
 * @version V1.0
 
 * @description: 
 * @date 2018/9/27 14:02
 */
public class BeanToMapConverterFactory extends Converter.Factory {

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
       //return new BeanToMapRequestBodyConverter<>(gson, adapter);
        return null;
    }
}
