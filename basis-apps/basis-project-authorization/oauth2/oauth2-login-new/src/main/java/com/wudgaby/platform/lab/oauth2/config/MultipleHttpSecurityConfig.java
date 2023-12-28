package com.wudgaby.platform.lab.oauth2.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/9/17 10:27
 * @Desc :
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class MultipleHttpSecurityConfig {
    /*@Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }*/

    @Order(1)
    @Configuration
    public static class ApiSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                    .authorizeRequests(author -> author.anyRequest().authenticated())
            ;
        }
    }

    /**
     * http
     *                 .authorizeRequests(authorize -> authorize
     *                         .anyRequest().authenticated()
     *                 )
     *                 .oauth2Login(oauth2 -> oauth2
     *                         .clientRegistrationRepository(this.clientRegistrationRepository())
     *                         .authorizedClientRepository(this.authorizedClientRepository())
     *                         .authorizedClientService(this.authorizedClientService())
     *                         .loginPage("/login/oauth2")
     *                         .authorizationEndpoint(authorization -> authorization
     *                                 .baseUri("/login/oauth2/authorization")
     *                                 .authorizationRequestRepository(this.authorizationRequestRepository())
     *                                 .authorizationRequestResolver(this.authorizationRequestResolver())
     *                         )
     *                         .redirectionEndpoint(redirection -> redirection
     *                                 .baseUri("/login/oauth2/callback/*")
     *                         )
     *                         .tokenEndpoint(token -> token
     *                                 .accessTokenResponseClient(this.accessTokenResponseClient())
     *                         )
     *                         .userInfoEndpoint(userInfo -> userInfo
     *                                 .userAuthoritiesMapper(this.userAuthoritiesMapper())
     *                                 .userService(this.oauth2UserService())
     *                                 .oidcUserService(this.oidcUserService())
     *                         )
     *                 )
     *                 .oauth2Client(oauth2 -> oauth2
     *                 .clientRegistrationRepository(this.clientRegistrationRepository())
     *                 .authorizedClientRepository(this.authorizedClientRepository())
     *                 .authorizedClientService(this.authorizedClientService())
     *                 .authorizationCodeGrant(codeGrant -> codeGrant
     *                         .authorizationRequestRepository(this.authorizationRequestRepository())
     *                         .authorizationRequestResolver(this.authorizationRequestResolver())
     *                         .accessTokenResponseClient(this.accessTokenResponseClient())
     *                 )
     *             )
     *         ;
     */
    @Configuration
    public static class GeneralSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf(csf -> csf.disable())
                    .authorizeRequests(author -> author
                            .antMatchers("/oauth2/login", "/login/oauth2/authorization","/login/oauth2/code/*").permitAll()
                            .anyRequest().authenticated())
                    //.formLogin(Customizer.withDefaults())
                    .oauth2Login(oauth2 -> oauth2
                            /*.loginPage("/oauth2/login")
                            .authorizationEndpoint(authorization -> authorization.baseUri("/login/oauth2/authorization"))
                            .redirectionEndpoint(redirection -> redirection.baseUri("/login/oauth2/code/*"))*/
                            .tokenEndpoint(Customizer.withDefaults())
                            .userInfoEndpoint(Customizer.withDefaults())
                            .successHandler((request, response, authentication) -> {
                                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                                Map<String, Object> attributes = oAuth2User.getAttributes();
                                // ....登录成功以后，既可获取用户信息

                                // ..跳转到成功页面
                                response.sendRedirect("/home");
                            })
                            .failureHandler((request, response, ex) -> {
                                log.error(ex.getMessage(), ex);
                            })
                    ).oauth2Client(
                            /*oauth2 -> oauth2
                            .clientRegistrationRepository(this.clientRegistrationRepository())
                            .authorizedClientRepository(this.authorizedClientRepository())
                            .authorizedClientService(this.authorizedClientService())
                            .authorizationCodeGrant(codeGrant -> codeGrant
                                    .authorizationRequestRepository(this.authorizationRequestRepository())
                                    .authorizationRequestResolver(this.authorizationRequestResolver())
                                    .accessTokenResponseClient(this.accessTokenResponseClient())
                            )*/
                        )
            ;
        }
    }
}
