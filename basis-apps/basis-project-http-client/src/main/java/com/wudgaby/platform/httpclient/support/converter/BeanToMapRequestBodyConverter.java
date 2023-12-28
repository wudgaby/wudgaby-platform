package com.wudgaby.platform.httpclient.support.converter;

import okhttp3.RequestBody;
import retrofit2.Converter;

import java.io.IOException;

/**
 * @author wudgaby
 * @version V1.0
 
 * @description: 
 * @date 2018/9/27 13:57
 */
public class BeanToMapRequestBodyConverter<T> implements Converter<T, RequestBody> {

    @Override
    public RequestBody convert(T value) throws IOException {
        return null;
    }
}
