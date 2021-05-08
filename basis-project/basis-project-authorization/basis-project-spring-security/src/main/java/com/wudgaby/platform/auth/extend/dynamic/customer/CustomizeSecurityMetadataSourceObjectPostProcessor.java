package com.wudgaby.platform.auth.extend.dynamic.customer;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @ClassName : CustomizeSecurityMetadataSourceObjectPostProcessor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/11 12:32
 * @Desc :   TODO
 */
public class CustomizeSecurityMetadataSourceObjectPostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor> {

    private SecurityConfigAttributeLoader securityConfigAttributeLoader;

    public CustomizeSecurityMetadataSourceObjectPostProcessor(SecurityConfigAttributeLoader securityConfigAttributeLoader) {
        this.securityConfigAttributeLoader = securityConfigAttributeLoader;
    }

    @Override
    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
        FilterSecurityInterceptor interceptor = object;

        CustomizeConfigSourceFilterInvocationSecurityMetadataSource metadataSource =
                new CustomizeConfigSourceFilterInvocationSecurityMetadataSource(
                        interceptor.obtainSecurityMetadataSource() , securityConfigAttributeLoader);
        interceptor.setSecurityMetadataSource(metadataSource);
        return (O) interceptor;
    }
}
