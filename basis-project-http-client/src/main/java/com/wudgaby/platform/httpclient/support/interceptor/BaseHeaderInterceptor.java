package com.wudgaby.platform.httpclient.support.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author wudgaby
 * @version V1.0
 * @ClassName: BaseHeaderInterceptor
 * @Description: TODO
 * @date 2018/9/27 14:53
 */
@Slf4j
@AllArgsConstructor
public class BaseHeaderInterceptor implements Interceptor {
    private final String userAgent;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request = originalRequest.newBuilder()
                .headers(originalRequest.headers())
                .removeHeader("User-Agent")
                .header("User-Agent", userAgent)
                //https://stackoverflow.com/questions/33889840/retrofit-and-okhttp-gzip-decode
                //.header("Accept-Encoding", "gzip, deflate")
                .header("Connection", "keep-alive")
                .header("Accept", "*/*")
                .method(originalRequest.method(), originalRequest.body())
                .build();

        return chain.proceed(request);
    }
}
