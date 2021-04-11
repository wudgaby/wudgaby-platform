package com.wudgaby.platform.httpclient.facotry;

import com.wudgaby.platform.httpclient.support.converter.fastjson.FastJsonConverterFactory;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @ClassName : RetrofitFactory
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/11/15/015 18:43
 * @Desc :   TODO
 */
@UtilityClass
public class RetrofitFactory {
    public static Retrofit createInstance(String baseUrl, OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(FastJsonConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
