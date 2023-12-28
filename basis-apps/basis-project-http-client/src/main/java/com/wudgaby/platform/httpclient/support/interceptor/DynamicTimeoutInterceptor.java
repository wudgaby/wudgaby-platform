package com.wudgaby.platform.httpclient.support.interceptor;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/3/21 18:24
 * @Desc :
 */

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 动态设置接口请求超时时间
 */
public class DynamicTimeoutInterceptor implements Interceptor {
    private static final int POLLING_READ_TIMEOUT = (int)TimeUnit.MINUTES.toSeconds(2);


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        String pooling = request.header("polling");
        boolean isPollingApi = StringUtils.isNotBlank(pooling);
        if (isPollingApi){
            return chain.withReadTimeout(POLLING_READ_TIMEOUT, TimeUnit.SECONDS).proceed(request);
        }
        return chain.proceed(request);
    }
}