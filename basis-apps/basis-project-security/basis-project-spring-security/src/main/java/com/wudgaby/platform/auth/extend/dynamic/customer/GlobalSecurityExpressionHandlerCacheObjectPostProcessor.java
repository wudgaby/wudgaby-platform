package com.wudgaby.platform.auth.extend.dynamic.customer;

import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.ObjectPostProcessor;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/11 12:30
 * @Desc :   
 */
public class GlobalSecurityExpressionHandlerCacheObjectPostProcessor implements ObjectPostProcessor<SecurityExpressionHandler> {

    private static SecurityExpressionHandler securityExpressionHandler;

    @Override
    public <O extends SecurityExpressionHandler> O postProcess(O object) {
        securityExpressionHandler = object;
        return object;
    }

    public static SecurityExpressionHandler getSecurityExpressionHandler() {
        return securityExpressionHandler;
    }
}
