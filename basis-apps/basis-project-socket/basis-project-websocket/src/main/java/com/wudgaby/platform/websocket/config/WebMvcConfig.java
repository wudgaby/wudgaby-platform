package com.wudgaby.platform.websocket.config;

import com.wudgaby.platform.websocket.handler.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/25 0:08
 * @Desc :
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor authInterceptor(){
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/login")
            ;
    }
}
