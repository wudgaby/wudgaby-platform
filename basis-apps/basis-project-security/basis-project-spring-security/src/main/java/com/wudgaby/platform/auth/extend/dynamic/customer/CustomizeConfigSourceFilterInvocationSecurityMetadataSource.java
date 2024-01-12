package com.wudgaby.platform.auth.extend.dynamic.customer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jooq.tools.reflect.Reflect;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/11 12:24
 * @Desc :
 */
public class CustomizeConfigSourceFilterInvocationSecurityMetadataSource extends DefaultFilterInvocationSecurityMetadataSource {

    private static final Reflect REFLECT = Reflect.onClass(ExpressionBasedFilterInvocationSecurityMetadataSource.class);

    private SecurityMetadataSource delegate;
    private SecurityConfigAttributeLoader metadataSourceLoader;
    private ExpressionParser expressionParser;

    public CustomizeConfigSourceFilterInvocationSecurityMetadataSource(
            SecurityMetadataSource delegate ,
            SecurityConfigAttributeLoader metadataSourceLoader) {
        super(new LinkedHashMap<>());
        this.delegate = delegate;
        this.metadataSourceLoader = metadataSourceLoader;

        copyDelegateRequestMap();
    }

    private void copyDelegateRequestMap() {
        Reflect reflect = Reflect.on(this);
        reflect.set("requestMap" , getDelegateRequestMap());
    }

    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> getDelegateRequestMap() {
        Reflect reflect = Reflect.on(this.delegate);
        return reflect.field("requestMap").get();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap =
                this.metadataSourceLoader.loadConfigAttribute(request);

        if (MapUtils.isEmpty(requestMap)) {
            configAttributes.addAll(this.delegate.getAttributes(object));
            return configAttributes;
        }

        if (Objects.isNull(this.expressionParser)) {
            SecurityExpressionHandler securityExpressionHandler = GlobalSecurityExpressionHandlerCacheObjectPostProcessor.getSecurityExpressionHandler();
            if (Objects.isNull(securityExpressionHandler)) {
                throw new NullPointerException(SecurityExpressionHandler.class.getName() + " is null");
            }
            this.expressionParser = securityExpressionHandler.getExpressionParser();
        }


        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> webExpressionRequestMap =
                REFLECT.call("processMap" , requestMap , this.expressionParser).get();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : webExpressionRequestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                configAttributes.addAll(entry.getValue());
                break;
            }
        }

        if (CollectionUtils.isEmpty(configAttributes)) {
            configAttributes.addAll(this.delegate.getAttributes(object));
        }

        return configAttributes;
    }
}
