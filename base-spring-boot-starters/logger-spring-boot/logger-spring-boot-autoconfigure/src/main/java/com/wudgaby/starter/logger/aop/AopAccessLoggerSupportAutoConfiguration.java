package com.wudgaby.starter.logger.aop;


import com.wudgaby.logger.api.AccessLoggerEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * AOP 访问日志记录自动配置
 */
@ConditionalOnClass(AccessLoggerEventListener.class)
@Configuration
public class AopAccessLoggerSupportAutoConfiguration {

    @Bean
    public AopAccessLoggerSupport aopAccessLoggerSupport() {
        return new AopAccessLoggerSupport();
    }

    @Bean
    @Order(-1)
    public DefaultAccessLoggerParser defaultAccessLoggerParser(){
        return new DefaultAccessLoggerParser();
    }

    @Bean
    @Order(1)
    @ConditionalOnClass(name = "io.swagger.annotations.Api")
    public SwaggerAccessLoggerParser swaggerAccessLoggerParser(){
        return new SwaggerAccessLoggerParser();
    }
}
