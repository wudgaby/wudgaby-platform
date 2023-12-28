package com.wudgaby.platform.httpclient.support.converter.jackson;

import com.wudgaby.platform.utils.JacksonUtil;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

import java.io.IOException;

public class JacksonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Class<T> type;

    public JacksonResponseBodyConverter(Class<T> type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        bufferedSource.close();
        return JacksonUtil.deserialize(tempStr, type);

    }
}
