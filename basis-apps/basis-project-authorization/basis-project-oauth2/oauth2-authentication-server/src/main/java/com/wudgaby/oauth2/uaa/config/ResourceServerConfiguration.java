package com.wudgaby.oauth2.uaa.config;

import com.wudgaby.oauth2.uaa.exception.AuthExceptionEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/24 19:06
 * @Desc :
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests()
                .antMatchers("/loginPage","/login","/loginProcess","/clientDetails").permitAll()
                .anyRequest().authenticated();*/

        http.requestMatchers().antMatchers("/api/**", "/user/**")
                            .and()
                            .authorizeRequests()
                            .antMatchers("/api/**").authenticated()
                            .antMatchers("/user/**").authenticated();

        /*http.requestMatchers()
                // For org.springframework.security.web.SecurityFilterChain.matches(HttpServletRequest)
                .requestMatchers(
                        new NegatedRequestMatcher(
                                new OrRequestMatcher(
                                        new AntPathRequestMatcher("/loginPage"),
                                        new AntPathRequestMatcher("/login"),
                                        new AntPathRequestMatcher("/logout"),
                                        new AntPathRequestMatcher("/oauth/authorize"),
                                        new AntPathRequestMatcher("/oauth/confirm_access")
                                )
                        )
                )
                .and()
                .authorizeRequests().anyRequest().authenticated();*/
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //资源服务器的自定义异常处理
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
        resources.accessDeniedHandler(customAccessDeniedHandler);
        //resources.resourceId("resource_id").tokenStore(tokenStore);
    }
}
