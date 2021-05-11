package com.wudgaby.platform.httpclient.facotry;

import com.google.common.collect.Lists;
import com.wudgaby.platform.httpclient.ProxyConfigVo;
import com.wudgaby.platform.httpclient.support.ProxyAuthenticator;
import com.wudgaby.platform.httpclient.support.cookie.CookieJarImpl;
import com.wudgaby.platform.httpclient.support.cookie.store.MemoryCookieStore;
import com.wudgaby.platform.httpclient.support.interceptor.BaseHeaderInterceptor;
import com.wudgaby.platform.utils.UserAgentUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import okhttp3.CipherSuite;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.CookieJar;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;

import java.net.Authenticator;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @author wudgaby
 * @version V1.0
 * @ClassName: OkHttpClientFactory
 * @Description: okhttpclient工厂
 * @date 2018/9/27 14:53
 */
@Slf4j
@UtilityClass
public class OkHttpClientFactory {
	public static boolean HTTP_DEBUG = false;
	public static HttpLoggingInterceptor.Level LOG_LEVEL = HttpLoggingInterceptor.Level.HEADERS;

	public OkHttpClient createInstance(){
		return createInstance(null);
	}

	public OkHttpClient createInstance(ProxyConfigVo proxyConfigVo){
		return createInstance(proxyConfigVo, null);
	}

	public OkHttpClient createInstance(ProxyConfigVo proxyConfigVo, String userAgent){
		OkHttpClient.Builder builder = defaultOkHttpBuilder(userAgent);
		configProxy(builder, proxyConfigVo);
		return builder.build();
	}

	private OkHttpClient.Builder defaultOkHttpBuilder(String userAgent){
		if (StringUtils.isBlank(userAgent)) {
			userAgent = UserAgentUtils.randomDesktopUserAgent();
		}
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectionPool(new ConnectionPool(5, 1, TimeUnit.MINUTES))
				.connectionSpecs(Lists.newArrayList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT, connectionSpec()))
				.cookieJar(memoryCookieStore())
				.followRedirects(true)
				.followSslRedirects(true)
				.addInterceptor(new BaseHeaderInterceptor(userAgent))
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS)
				.writeTimeout(20, TimeUnit.SECONDS)
				.retryOnConnectionFailure(true)
				.sslSocketFactory(new ProxySSLSocketFactory(null, SSLSocketHelper.createSSLSocketFactory()) , SSLSocketHelper.createTrustManager())
				.socketFactory(new ProxySocketFactory(null))
				.hostnameVerifier(SSLSocketHelper.createHostnameVerifier())
				;

		if(HTTP_DEBUG){
			builder.addNetworkInterceptor(httpLoggingInterceptor());
		}
		return builder;
	}

	/**
	 * 配置代理
	 * @param builder
	 * @param proxyConfigVo
	 */
	private void configProxy(OkHttpClient.Builder builder, ProxyConfigVo proxyConfigVo){
		if(proxyConfigVo != null){
			if(proxyConfigVo.getProxy() != null){
				builder.proxy(proxyConfigVo.getProxy());
				//builder.sslSocketFactory(new ProxySSLSocketFactory(proxyConfigVo.getProxy(), SSLSocketHelper.createSSLSocketFactory()) , SSLSocketHelper.createTrustManager());
				//builder.socketFactory(new ProxySocketFactory(proxyConfigVo.getProxy()));
			}

			if(StringUtils.isNotBlank(proxyConfigVo.getUserName())
					&& StringUtils.isNotBlank(proxyConfigVo.getPassword())){

				//全局
				if(proxyConfigVo.getProxy().type() == Proxy.Type.SOCKS){
					Authenticator.setDefault(new ProxyAuthenticator(proxyConfigVo.getUserName(), proxyConfigVo.getPassword()));
				}

				//http代理才有效
				builder.proxyAuthenticator((route, response) ->{
					String credential = Credentials.basic(proxyConfigVo.getUserName(), proxyConfigVo.getPassword());
					return response.request().newBuilder()
							.header("Proxy-Authorization", credential)
							.build();
				});
			}
		}
	}

	/**
	 * 内存存储cookie
	 * @return
	 */
	private static CookieJar memoryCookieStore(){
		return new CookieJarImpl(new MemoryCookieStore());
	}

	private static ConnectionSpec connectionSpec(){
		return new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
				.tlsVersions(TlsVersion.TLS_1_2)
				.cipherSuites(
						CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
						CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
						CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
				.build();
	}

	/**
	 * 日志拦截
	 * @return
	 */
	private Interceptor httpLoggingInterceptor(){
		HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
		httpLoggingInterceptor.setLevel(LOG_LEVEL);
		return httpLoggingInterceptor;
	}
}
