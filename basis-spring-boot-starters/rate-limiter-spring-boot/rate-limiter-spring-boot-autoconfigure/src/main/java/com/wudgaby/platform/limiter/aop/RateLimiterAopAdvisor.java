package com.wudgaby.platform.limiter.aop;

import com.wudgaby.platform.limiter.core.LimitException;
import com.wudgaby.platform.limiter.core.annotation.RateLimiter;
import com.wudgaby.platform.limiter.service.RateLimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Optional;


/**
 * @ClassName : RateLimiterAopAdvisor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/30 22:40
 * @Desc :
 */
@Slf4j
public class RateLimiterAopAdvisor extends StaticMethodMatcherPointcutAdvisor {
    private static final long serialVersionUID = 1;

    public RateLimiterAopAdvisor(RateLimiterService rateLimiterService) {
        setAdvice((MethodBeforeAdvice) (method, args, target) -> {
            RateLimiter rateLimiter = Optional.ofNullable(AnnotationUtils.findAnnotation(method, RateLimiter.class))
                    .orElseGet(() -> AnnotationUtils.findAnnotation(ClassUtils.getUserClass(target), RateLimiter.class));
            if (rateLimiter != null) {
                boolean isAllowed = rateLimiterService.isAllowed(rateLimiter, method, args);
                if (!isAllowed) {
                    throw new LimitException();
                }
            }
        });
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return AnnotationUtils.findAnnotation(method, RateLimiter.class) != null
                || AnnotationUtils.findAnnotation(targetClass, RateLimiter.class) != null;
    }
}
