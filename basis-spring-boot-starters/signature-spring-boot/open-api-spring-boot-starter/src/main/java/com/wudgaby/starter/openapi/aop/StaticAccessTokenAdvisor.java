package com.wudgaby.starter.openapi.aop;


import com.wudgaby.platform.core.aop.MethodInterceptorHolder;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.starter.openapi.annotations.AccessTokenVerifier;
import com.wudgaby.starter.redis.support.RedisSupport;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author wudgaby
 */
@Component
public class StaticAccessTokenAdvisor extends StaticMethodMatcherPointcutAdvisor {
    private static final String ACCESS_TOKEN = "accessToken";

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

            String paramAccessToken = getRequest().getParameter(ACCESS_TOKEN);
            String headerAccessToken = getRequest().getHeader(ACCESS_TOKEN);

            String accessToken = StringUtils.isBlank(paramAccessToken) ? headerAccessToken : paramAccessToken;
            if(StringUtils.isBlank(accessToken)){
                throw new BusinessException("缺少" +ACCESS_TOKEN+ "参数");
            }

            Object result = redisSupport.get(accessToken);
            if(result == null){
                throw new BusinessException("无效的" + ACCESS_TOKEN);
            }
            Object response = methodInvocation.proceed();
            return response;
        };
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }
}
