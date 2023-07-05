package com.wudgaby.platform.httpclient.support.converter.jackson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class JacksonConverterFactory extends Converter.Factory{

    public static JacksonConverterFactory create() {
        return new JacksonConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new JacksonResponseBodyConverter<>(type.getClass());
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new JacksonRequestBodyConverter<>();
    }

}