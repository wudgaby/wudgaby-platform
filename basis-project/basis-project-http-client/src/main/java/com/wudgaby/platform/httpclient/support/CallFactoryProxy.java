package com.wudgaby.platform.httpclient.support;

/**
 * @ClassName : CallFactoryProxy
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/21 18:33
 * @Desc :   TODO
 */

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;

import javax.annotation.Nullable;

/**
 * 功能描述：代理{@link Call.Factory} 拦截{@link #newCall(Request)}方法
 * @Header(CallFactoryProxy.BASE_URL_NAME)
*                 Retrofit.Builder.callFactory(new CallFactoryProxy(okHttpClient) {
*                     @Override
*                     protected HttpUrl getNewBaseUrl(String baseUrlName, Request request) {
*                         if (baseUrlName.equals("baidu")) {
*                             String oldUrl = request.url().toString();
*                             String newUrl = oldUrl.replace("https://wanandroid.com/", "https://www.baidu.com/");
*                             return HttpUrl.get(newUrl);
*                         }
*                         return null;
*                     }
*                 })
 */
@Slf4j
public abstract class CallFactoryProxy implements Call.Factory {
    public static final String BASE_URL_NAME = "BaseUrlName";
    private final Call.Factory delegate;

    public CallFactoryProxy(Call.Factory delegate) {
        this.delegate = delegate;
    }

    @Override
    public Call newCall(Request request) {
        String baseUrlName = request.header(BASE_URL_NAME);

        if (baseUrlName != null) {
            HttpUrl newHttpUrl = getNewBaseUrl(baseUrlName, request);
            if (newHttpUrl != null) {
                Request newRequest = request.newBuilder().url(newHttpUrl).build();
                return delegate.newCall(newRequest);
            } else {
                log.error("newHttpUrl is null. when baseUrlName : {}", baseUrlName);
            }
        }
        return delegate.newCall(request);
    }

    /**
     * 获取新baseUrl
     * @param baseUrlName
     * @param request
     * @return
     */
    @Nullable
    protected abstract HttpUrl getNewBaseUrl(String baseUrlName, Request request);
}
