package com.wudgaby.platform.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/26 19:32
 * @Desc :
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class HttpSessionConfig {
    @Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        return new CookieHttpSessionIdResolver();
    }

    /*@Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        return new HeaderHttpSessionIdResolver(SysConsts.X_AUTH_TOKEN);
    }*/
}
