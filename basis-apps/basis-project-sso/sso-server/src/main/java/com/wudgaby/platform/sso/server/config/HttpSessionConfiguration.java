package com.wudgaby.platform.sso.server.config;

import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.server.support.HeaderCookieHttpSessionIdResolver;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/12/8 16:20
 * @Desc :
 */
@Configuration
public class HttpSessionConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean("httpSessionIdResolver")
    public HeaderCookieHttpSessionIdResolver httpSessionIdResolver(@Value("${server.servlet.session.cookie.name:SESSION}") String cookieName) {
        HeaderCookieHttpSessionIdResolver headerCookieHttpSessionIdResolver = new HeaderCookieHttpSessionIdResolver(SsoConst.SSO_HEADER_X_TOKEN);
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setSameSite(SameSiteCookies.LAX.getValue());
        cookieSerializer.setUseSecureCookie(false);
        cookieSerializer.setUseBase64Encoding(false);
        cookieSerializer.setCookieName(cookieName);
        headerCookieHttpSessionIdResolver.setCookieSerializer(cookieSerializer);
        return headerCookieHttpSessionIdResolver;
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
