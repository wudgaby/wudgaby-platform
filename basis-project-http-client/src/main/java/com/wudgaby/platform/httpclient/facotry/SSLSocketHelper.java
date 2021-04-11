package com.wudgaby.platform.httpclient.facotry;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @ClassName : SSLSocketHelper
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/21 18:52
 * @Desc :   设置https 访问的时候对所有证书都进行信任
 */
@Slf4j
@UtilityClass
public class SSLSocketHelper {

    public static SSLSocketFactory createSSLSocketFactory(){
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, new TrustManager[]{createTrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
           log.error(e.getMessage(), e);
        }
        return null;
    }

    public static X509TrustManager createTrustManager(){
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        return trustManager;
    }

    public static HostnameVerifier createHostnameVerifier(){
        return (hostname, session) -> true;
    }
}
