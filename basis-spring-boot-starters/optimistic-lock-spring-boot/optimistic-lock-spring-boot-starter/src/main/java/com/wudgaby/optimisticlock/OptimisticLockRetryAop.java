package com.wudgaby.optimisticlock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/2/22 0022 10:17
 * @desc :  数据库乐观锁重试切面
 * https://github.com/alldays/spring-cloud-mall
 * https://www.jianshu.com/p/2b14637d81f8
 */
@Slf4j
@Aspect
@Component
@Order(100)
public class OptimisticLockRetryAop {
    /**
     * 切入点
     * @annotation 标识具体方法，@within 标识具体类
     */
    @Pointcut("@annotation(OptimisticLockRetry) || @within(OptimisticLockRetry)")
    public void optimisticRetryPointcut() {

    }

    @Around("optimisticRetryPointcut()")
    public Object doConcurrentOperation(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 代理目标对象Class
        Class targetClazz = joinPoint.getTarget().getClass();
        Method method = targetClazz.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        // 乐观锁重试
        Optional<OptimisticLockRetry> optimisticRetryOption = getOptimisticRetry(method.getAnnotations());
        if (!optimisticRetryOption.isPresent()) {
            return joinPoint.proceed();
        }
        OptimisticLockRetry optimisticLockRetry = optimisticRetryOption.get();
        int numAttempts = 0;
        OptimisticLockException lockFailureException;
        long startTime = System.currentTimeMillis();
        do {
            numAttempts++;
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockException ex) {
                lockFailureException = ex;
                long executeTime = System.currentTimeMillis() - startTime;
                long maxExecuteTime = optimisticLockRetry.maxExecuteTime();

                if (log.isDebugEnabled()) {
                    log.debug("execute optimistic retry lock!count {},time {}!max try count {},max execute time {}!", numAttempts, maxExecuteTime,
                            optimisticLockRetry.maxExecuteTime(), maxExecuteTime);
                }

                // 乐观锁重试时间限制
                if (isLargerThanMaxExecuteTime(executeTime, maxExecuteTime)) {
                    // 超过乐观锁时间长度控制，抛出乐观锁异常
                    log.warn("throw optimistic locking failure exception!num attempts [{}],start time [{}]," +
                                    "actual execute time [{}] ms, max execute time [{}] ms",
                            numAttempts, startTime, executeTime, maxExecuteTime);
                    throw lockFailureException;
                }
            }
        }
        // 乐观锁重试次数限制
        while (numAttempts <= optimisticLockRetry.value());

        // 超过乐观锁重试次数，抛出乐观锁异常
        log.warn("throw optimistic locking failure exception!num attempts {} ", numAttempts);
        throw lockFailureException;
    }

    /**
     * 大于限制的重试执行时间
     *
     * @param executeTime
     * @return
     */
    private boolean isLargerThanMaxExecuteTime(long executeTime, long maxExecuteTime) {
        if (executeTime <= 0) {
            return false;
        }
        if (executeTime > maxExecuteTime) {
            return true;
        }
        return false;
    }

    private Optional<OptimisticLockRetry> getOptimisticRetry(Annotation[] annotations) {

        if (ArrayUtils.isEmpty(annotations)) {
            return Optional.empty();
        }
        for (Annotation anno : annotations) {
            if (anno.annotationType().isAssignableFrom(OptimisticLockRetry.class)) {
                return Optional.of((OptimisticLockRetry) anno);
            }
        }
        return Optional.empty();
    }
}
