package com.wudgaby.sample.oauth2server.config;

import com.wudgaby.sample.oauth2server.oauth2extend.GiteeOAuth2UserService;
import com.wudgaby.sample.oauth2server.oauth2extend.Oauth2LoginFailedHandler;
import com.wudgaby.sample.oauth2server.oauth2extend.Oauth2LoginSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/8 0008 18:18
 * @desc : Spring Security 配置
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class DefaultSecurityConfig {
    public static final String LOGIN_PAGE = "/ssologin";
    public static final String LOGOUT_PAGE = "/ssologout";

    private final GiteeOAuth2UserService giteeOAuth2UserService;
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final Oauth2LoginFailedHandler oauth2LoginFailedHandler;
    private final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;


    /**
     * Spring Security 的过滤器链，用于 Spring Security 的身份认证
     * 授权服务中也提供了资源服务；如：用户信息 /oauth2/user ，在认证授权后，可以通过该接口，获得用户信息。
     * 如果把该资源服务剥离出去，就可以去掉与资源服务相关的内容：cer公钥、解码器方法、user端口API等；
     */
    @Bean
    @Order(1)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置 请求授权
        http.authorizeRequests(authorizeRequests -> authorizeRequests
                        //放行
                        .antMatchers(LOGIN_PAGE).permitAll()
                        //也提供资源服务
                        //.antMatchers("/oauth2/user").hasAnyAuthority("SCOPE_profile","SCOPE_all")
                        // 任何请求都需要认证（不对未登录用户开放）
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                //.formLogin(Customizer.withDefaults())
                // 表单登录, loginProcessingUrl不配置时与loginPage一致, 也影响logout路径.
                .formLogin(formLoginConfigurer -> formLoginConfigurer.loginPage(LOGIN_PAGE))
                //默认会启动CSRF防护，一旦启动了CSRF防护，“/logout” 需要用post的方式提交.
                .logout(logoutConfigurer -> logoutConfigurer.logoutUrl(LOGOUT_PAGE))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                // oauth2三方登录
                //.oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
        ;

        //OAuth2UserService, OAuth2User
        http.oauth2Login()
                .loginPage(LOGIN_PAGE)
                //.authorizedClientService(oAuth2AuthorizedClientService)
                //.authorizedClientRepository(oAuth2AuthorizedClientRepository)
                //.userInfoEndpoint().userService(giteeOAuth2UserService)
                //.and()
                //登录成功/失败的逻辑处理
                //.successHandler(oauth2LoginSuccessHandler)
                //.failureHandler(oauth2LoginFailedHandler)

        ;

        return http.build();
    }

    @Bean
    public UserDetailsManager users() {
        UserDetails user = User.builder()
                .username("admin")
                .password("123456")
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .roles("USER")
                //.authorities("SCOPE_userinfo")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /*
    @SneakyThrows
    @Bean
    JwtDecoder jwtDecoder() {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("x.509");
        // 读取cer公钥证书来配置解码器
        ClassPathResource resource = new ClassPathResource("jks/my.cer");
        Certificate certificate = certificateFactory.generateCertificate(resource.getInputStream());
        RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }*/
}
