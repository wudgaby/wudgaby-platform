package com.wudgaby.platform.auth.extend.dynamic.customer;

import org.jooq.tools.reflect.Reflect;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @ClassName : CustomizeSecurityConfigAttributeSourceConfigurer
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/11 12:31
 * @Desc :   TODO
 */
public class CustomizeSecurityConfigAttributeSourceConfigurer {
    public static <T extends ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry> T enable(
            T configurer
            , SecurityConfigAttributeLoader securityConfigAttributeLoader) {
        Assert.notNull(configurer , "configurer must not be null");
        Assert.notNull(securityConfigAttributeLoader , "securityConfigAttributeLoader must not be null");

        configurer.withObjectPostProcessor(createSecurityMetadataSourcePostProcessor(securityConfigAttributeLoader))
                .withObjectPostProcessor(createSecurityExpressionHandlerPostProcessor());

        return configurer;
    }

    public static <T extends ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry> T enable(T configurer) {
        Assert.notNull(configurer , "configurer must not be null");
        if (Objects.isNull(configurer)) {
            throw new NullPointerException(ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry.class.getName() + " is null");
        }

        SecurityConfigAttributeLoader securityConfigAttributeLoader = getSecurityConfigAttributeLoaderFromIocContainer(configurer);
        return enable(configurer , securityConfigAttributeLoader);
    }

    private static <T extends ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry> SecurityConfigAttributeLoader getSecurityConfigAttributeLoaderFromIocContainer(T configurer) {
        ApplicationContext context = Reflect.on(configurer).get("context");
        SecurityConfigAttributeLoader securityConfigAttributeLoader = context.getBean(SecurityConfigAttributeLoader.class);
        if (Objects.isNull(securityConfigAttributeLoader)) {
            throw new RuntimeException("No instances found from the spring context , Class : " + SecurityConfigAttributeLoader.class);
        }
        return securityConfigAttributeLoader;
    }

    public static CustomizeSecurityMetadataSourceObjectPostProcessor createSecurityMetadataSourcePostProcessor(SecurityConfigAttributeLoader securityConfigAttributeLoader) {
        return new CustomizeSecurityMetadataSourceObjectPostProcessor(securityConfigAttributeLoader);
    }

    public static GlobalSecurityExpressionHandlerCacheObjectPostProcessor createSecurityExpressionHandlerPostProcessor() {
        return new GlobalSecurityExpressionHandlerCacheObjectPostProcessor();
    }
}
