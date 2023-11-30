package com.wudgaby.starter.apiversion;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * @author
 */
@Slf4j
@AllArgsConstructor
public class VersionedRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    /**
     * 多版本配置属性
     */
    private ApiVersionProperties apiVersionProperties;

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return createRequestCondition(handlerType);
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return createRequestCondition(method);
    }

    private RequestCondition<ApiVersionRequestCondition> createRequestCondition(AnnotatedElement target) {
        if (apiVersionProperties.getType() == ApiVersionProperties.Type.URI) {
            return null;
        }
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(target, ApiVersion.class);
        IgnoreApiVersion ignoreApiVersion = AnnotationUtils.getAnnotation(target, IgnoreApiVersion.class);
        if (apiVersion == null || ignoreApiVersion != null) {
            return null;
        }
        String version = apiVersion.value().trim();
        InnerUtils.checkVersionNumber(version, target);
        return new ApiVersionRequestCondition(version, apiVersionProperties);
    }

    /**
     * --------------------- 动态注册URI -----------------------
     */
    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestCondition<?> condition = getRequestCondition(method, handlerType);
        RequestMappingInfo info = this.createRequestMappingInfo(method, condition);
        if (info != null) {
            RequestMappingInfo typeInfo = this.createRequestMappingInfo(handlerType, condition);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }

            // 指定URL前缀
            if (apiVersionProperties.getType() == ApiVersionProperties.Type.URI) {
                ApiVersion apiVersion = AnnotationUtils.getAnnotation(method, ApiVersion.class);
                IgnoreApiVersion ignoreApiVersion = AnnotationUtils.getAnnotation(method, IgnoreApiVersion.class);
                if (apiVersion == null) {
                    apiVersion = AnnotationUtils.getAnnotation(handlerType, ApiVersion.class);
                }
                if (apiVersion != null && ignoreApiVersion == null) {
                    String version = apiVersion.value().trim();
                    InnerUtils.checkVersionNumber(version, method);

                    String prefix = "/v" + version;
                    if (apiVersionProperties.getUriLocation() == ApiVersionProperties.UriLocation.END) {
                        info = info.combine(RequestMappingInfo.paths(new String[]{prefix}).build());
                    } else {
                        if (!StringUtils.isEmpty(apiVersionProperties.getUriPrefix())) {
                            prefix = apiVersionProperties.getUriPrefix().trim() + prefix;
                        }
                        info = RequestMappingInfo.paths(new String[]{prefix}).build().combine(info);
                    }
                }
            }
            log.info("[API-VERSION] {}", info);
        }

        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        RequestCondition<?> condition = element instanceof Class ? this.getCustomTypeCondition((Class) element) : this.getCustomMethodCondition((Method) element);
        log.info("{}, {}, {}", element, requestMapping, condition);
        return requestMapping != null ? this.createRequestMappingInfo(requestMapping, condition) : null;
    }

    private RequestCondition<?> getRequestCondition(Method method, Class<?> handlerType){
        RequestCondition<?> condition = this.getCustomMethodCondition(method);
        IgnoreApiVersion ignoreApiVersion = AnnotationUtils.getAnnotation(method, IgnoreApiVersion.class);
        if(condition == null && ignoreApiVersion == null) {
            condition = this.getCustomTypeCondition(handlerType);
        }
        return condition;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element, RequestCondition<?> condition) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        return requestMapping != null ? this.createRequestMappingInfo(requestMapping, condition) : null;
    }
}