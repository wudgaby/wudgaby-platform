package com.wudgaby.sample.oauth2server.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/11 0011 20:08
 * @desc : oauth2 client 配置. https://blog.csdn.net/qq_43430343/article/details/130271536
 */
@Deprecated
@Configuration(proxyBeanMethods = false)
public class OAuth2AuthorizedClientConfig {

    /**
     * 默认 HttpSessionOAuth2AuthorizedClientRepository 实现
     */
    /*@Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }*/


    /**
     * 默认 InMemoryOAuth2AuthorizedClientService 实现
     */
    /*@Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            JdbcOperations jdbcOperations, ClientRegistrationRepository clientRegistrationRepository) {
        //org/springframework/security/oauth2/client/oauth2-client-schema.sql
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
    }*/
}
