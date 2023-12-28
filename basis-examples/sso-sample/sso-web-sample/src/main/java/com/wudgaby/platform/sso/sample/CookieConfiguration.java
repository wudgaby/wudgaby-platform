package com.wudgaby.platform.sso.sample;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/9/16 9:37
 * @Desc :
 */
@Configuration
public class CookieConfiguration {
    @Bean
    public CookieSerializer cookieSerializer(@Value("${server.servlet.session.cookie.name:SESSION}") String cookieName) {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        // 取消仅限同一站点设置
        cookieSerializer.setCookieName(cookieName);
        cookieSerializer.setSameSite(SameSiteCookies.NONE.getValue());
        cookieSerializer.setUseSecureCookie(true);
        cookieSerializer.setUseBase64Encoding(false);
        return cookieSerializer;
    }
}