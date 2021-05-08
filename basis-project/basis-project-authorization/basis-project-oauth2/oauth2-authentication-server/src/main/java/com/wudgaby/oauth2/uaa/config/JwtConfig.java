package com.wudgaby.oauth2.uaa.config;

import com.wudgaby.platform.security.core.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : JwtConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/23 18:42
 * @Desc :   TODO
 */
@Configuration
public class JwtConfig {

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 非对称加密
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("my-jwt.jks"), "pwd123".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("my-jwt"));
        return converter;
    }

    @Bean(name = "myTokenEnhancer")
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(8);
            UserInfo user = (UserInfo) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("account", user.getAccount());
            additionalInfo.put("username", user.getUsername());
            additionalInfo.put("roles", user.getAuthorities());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }
}