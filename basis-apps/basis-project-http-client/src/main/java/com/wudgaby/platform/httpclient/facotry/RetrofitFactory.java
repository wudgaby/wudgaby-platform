package com.wudgaby.platform.httpclient.facotry;

import com.wudgaby.platform.httpclient.support.converter.jackson.JacksonConverterFactory;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/11/15/015 18:43
 * @Desc :
 */
@UtilityClass
public class RetrofitFactory {
    public static Retrofit createInstance(String baseUrl, OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
