package com.wudgaby.platform.webcore.configuration;

import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : TomcatConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/29 16:56
 * @Desc :   TODO
 */
@Configuration
public class TomcatConfiguration {

    /*@Bean
    public ConfigurableServletWebServerFactory webServerFactory(final GracefulShutdown gracefulShutdown) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(gracefulShutdown);
        return tomcat;
    }*/
}
