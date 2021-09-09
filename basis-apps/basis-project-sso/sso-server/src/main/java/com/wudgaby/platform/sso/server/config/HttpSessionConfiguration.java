package com.wudgaby.platform.sso.server.config;

import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.server.support.HeaderCookieHttpSessionIdResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : HttpSessionConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/12/8 16:20
 * @Desc :
 */
@Configuration
public class HttpSessionConfiguration {

    @Bean("httpSessionIdResolver")
    public HeaderCookieHttpSessionIdResolver httpSessionIdResolver() {
        return new HeaderCookieHttpSessionIdResolver(SsoConst.SSO_HEADER_X_TOKEN);
    }
}
