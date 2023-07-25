package com.wudgaby.starter.multiports;

import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.TomcatServletWebServerFactoryCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/19 0019 14:32
 * @desc :
 */
@ConditionalOnClass(Tomcat.class)
@ConditionalOnWebApplication
@EnableConfigurationProperties(MultiProp.class)
@Configuration
@AllArgsConstructor
public class MultiPortConfiguration {

    private final MultiProp multiProp;

    @Bean
    public FilterRegistrationBean trustedEndpointsFilterRegBean(TrustedEndpointsFilter trustedEndpointsFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(trustedEndpointsFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    @Bean
    public TrustedEndpointsFilter trustedEndpointsFilter(@Nullable Predicate<String> excludedPathPredicate){
        return new TrustedEndpointsFilter(multiProp.getTrustedPort(), multiProp.getTrustedPathPrefix(), excludedPathPredicate);
    }

    @Bean
    public WebServerFactoryCustomizer servletContainer() {
        Connector[] additionalConnectors = this.additionalConnector();

        ServerProperties serverProperties = new ServerProperties();
        return new TomcatMultiConnectorServletWebServerFactoryCustomizer(serverProperties, additionalConnectors);
    }

    private Connector[] additionalConnector() {
        if (StringUtils.isEmpty(multiProp.getTrustedPort())) {
            return null;
        }

        Set<String> defaultPorts = new HashSet<>();
        defaultPorts.add(multiProp.getPort());
        defaultPorts.add(multiProp.getManagementPort());

        if (!defaultPorts.contains(multiProp.getTrustedPort())) {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(Integer.valueOf(multiProp.getTrustedPort()));
            return new Connector[]{connector};
        } else {
            return new Connector[]{};
        }
    }

    private class TomcatMultiConnectorServletWebServerFactoryCustomizer extends TomcatServletWebServerFactoryCustomizer {
        private final Connector[] additionalConnectors;

        TomcatMultiConnectorServletWebServerFactoryCustomizer(ServerProperties serverProperties, Connector[] additionalConnectors) {
            super(serverProperties);
            this.additionalConnectors = additionalConnectors;
        }

        @Override
        public void customize(TomcatServletWebServerFactory factory) {
            super.customize(factory);

            if (additionalConnectors != null && additionalConnectors.length > 0) {
                factory.addAdditionalTomcatConnectors(additionalConnectors);
            }
        }
    }
}
