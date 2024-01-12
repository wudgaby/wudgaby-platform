package com.wudgaby.sample.oauth2resource01.config;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/9 0009 19:55
 * @desc :
 */
@EnableConfigurationProperties(JwtProperties.class)
@Configuration(proxyBeanMethods = false)
public class JwtDecoderConfiguration {
    /**
     * 注入 JwtProperties 属性配置类
     */
    @Autowired
    private JwtProperties jwtProperties;

    /**
     *  校验jwt发行者 issuer 是否合法
     *
     * @return the jwt issuer validator
     */
    @Bean
    JwtIssuerValidator jwtIssuerValidator() {
        return new JwtIssuerValidator(this.jwtProperties.getClaims().getIssuer());
    }

    /**
     *  校验jwt是否过期
     *
     * @return the jwt timestamp validator
     */
/*    @Bean
    JwtTimestampValidator jwtTimestampValidator() {
        System.out.println("检测令牌是否过期！"+ LocalDateTime.now());
        return new JwtTimestampValidator(Duration.ofSeconds((long) this.jwtProperties.getClaims().getExpiresAt()));
    }*/

    /**
     * jwt token 委托校验器，集中校验的策略{@link OAuth2TokenValidator}
     *
     * // @Primary：自动装配时当出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常
     * @param tokenValidators the token validators
     * @return the delegating o auth 2 token validator
     */
    @Primary
    @Bean({"delegatingTokenValidator"})
    public DelegatingOAuth2TokenValidator<Jwt> delegatingTokenValidator(Collection<OAuth2TokenValidator<Jwt>> tokenValidators) {
        return new DelegatingOAuth2TokenValidator<>(tokenValidators);
    }

    /**
     * 基于Nimbus的jwt解码器，并增加了一些自定义校验策略
     *
     * // @Qualifier 当有多个相同类型的bean存在时，指定注入
     * @param validator DelegatingOAuth2TokenValidator<Jwt> 委托token校验器
     * @return the jwt decoder
     */
    @SneakyThrows
    @Bean
    public JwtDecoder jwtDecoder(@Qualifier("delegatingTokenValidator")
                                 DelegatingOAuth2TokenValidator<Jwt> validator) {
        // 指定 X.509 类型的证书工厂
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        // 读取cer公钥证书来配置解码器
        String publicKeyLocation = this.jwtProperties.getCertInfo().getPublicKeyLocation();
        // 获取证书文件输入流
        ClassPathResource resource = new ClassPathResource(publicKeyLocation);
        InputStream inputStream = resource.getInputStream();
        // 得到证书
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
        // 解析
        RSAKey rsaKey = RSAKey.parse(certificate);
        // 得到公钥
        RSAPublicKey key = rsaKey.toRSAPublicKey();
        // 构造解码器
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withPublicKey(key).build();
        // 注入自定义JWT校验逻辑
        nimbusJwtDecoder.setJwtValidator(validator);
        return nimbusJwtDecoder;
    }
}
