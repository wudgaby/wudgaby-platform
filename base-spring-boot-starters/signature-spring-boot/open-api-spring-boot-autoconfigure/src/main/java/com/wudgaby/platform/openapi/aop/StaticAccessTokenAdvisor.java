package com.wudgaby.platform.openapi.aop;


import com.wudgaby.platform.core.aop.MethodInterceptorHolder;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.openapi.annotations.AccessTokenVerifier;
import com.wudgaby.platform.webcore.support.RequestContextHolderSupport;
import com.wudgaby.redis.api.RedisSupport;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class StaticAccessTokenAdvisor extends StaticMethodMatcherPointcutAdvisor {
    @Autowired
    private RedisSupport redisSupport;

    public StaticAccessTokenAdvisor() {
        setAdvice(aroundAdvice());
    }


    @Override
    public boolean matches(Method method, Class clazz) {
        AccessTokenVerifier methodAnn = AnnotationUtils.findAnnotation(method, AccessTokenVerifier.class);
        AccessTokenVerifier clazzAnn =  AnnotationUtils.findAnnotation(clazz, AccessTokenVerifier.class);
        return null != methodAnn ||  null != clazzAnn;
    }

    /**
     * 环绕通知
     * @return
     */
    protected Advice aroundAdvice(){
        return (MethodInterceptor) methodInvocation -> {
            //封装aop当前环境
            MethodInterceptorHolder methodInterceptorHolder = MethodInterceptorHolder.create(methodInvocation);

            //校验accessToken
            //String accessToken = String.valueOf(methodInterceptorHolder.getArgs().get("accessToken"));

            String paramAccessToken = RequestContextHolderSupport.getRequest().getParameter("accessToken");
            String headerAccessToken = RequestContextHolderSupport.getRequest().getHeader("accessToken");

            String accessToken = StringUtils.isBlank(paramAccessToken) ? headerAccessToken : paramAccessToken;
            if(StringUtils.isBlank(accessToken)){
                throw new BusinessException("缺少accessToken参数");
            }

            Object result = redisSupport.get(accessToken);
            if(result == null){
                throw new BusinessException("无效的accessToken");
            }
            Object response = methodInvocation.proceed();
            return response;
        };
    }
}
