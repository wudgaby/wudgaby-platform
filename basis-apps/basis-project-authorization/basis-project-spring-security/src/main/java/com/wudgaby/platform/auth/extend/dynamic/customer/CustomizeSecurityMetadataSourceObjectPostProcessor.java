package com.wudgaby.platform.auth.extend.dynamic.customer;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/11 12:32
 * @Desc :
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
