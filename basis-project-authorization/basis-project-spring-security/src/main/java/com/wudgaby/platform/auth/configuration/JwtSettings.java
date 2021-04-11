package com.wudgaby.platform.auth.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtSettings {
    /**
     * {@link com.wudgaby.platform.auth.model.token.JwtToken} will expire after this time.
     */
    private Integer tokenExpirationTime;

    /**
     * Token issuer.
     */
    private String tokenIssuer;
    
    /**
     * Key is used to sign {@link com.wudgaby.platform.auth.model.token.JwtToken}.
     */
    private String tokenSigningKey;
    
    /**
     * {@link com.wudgaby.platform.auth.model.token.JwtToken} can be refreshed during this timeframe.
     */
    private Integer refreshTokenExpTime;
}
