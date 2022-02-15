package com.wudgaby.starter.logger.aop;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.servlet.ServletUtil;
import com.wudgaby.logger.api.AccessLoggerEventListener;
import com.wudgaby.logger.api.event.AccessLoggerAfterEvent;
import com.wudgaby.logger.api.event.AccessLoggerBeforeEvent;
import com.wudgaby.logger.api.vo.AccessLoggerInfo;
import com.wudgaby.logger.api.vo.LoggerDefine;
import com.wudgaby.platform.core.aop.MethodInterceptorHolder;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用AOP记录访问日志,并触发{@link AccessLoggerEventListener#onLogger(AccessLoggerInfo)}
 *
 * @author zhouhao
 * @since 3.0
 */
public class AopAccessLoggerSupport extends StaticMethodMatcherPointcutAdvisor {

    @Autowired(required = false)
    private final List<AccessLoggerEventListener> listeners = new ArrayList<>();

    @Autowired(required = false)
    private final List<AccessLoggerParser> loggerParsers = new ArrayList<>();

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    public AopAccessLoggerSupport addListener(AccessLoggerEventListener loggerListener) {
        if (!listeners.contains(loggerListener)) {
            listeners.add(loggerListener);
        }
        return this;
    }

    public AopAccessLoggerSupport addParser(AccessLoggerParser parser) {
        if (!loggerParsers.contains(parser)) {
            loggerParsers.add(parser);
        }
        return this;
    }

    public AopAccessLoggerSupport() {
        setAdvice(aroundAdvice());
    }

    /**
     * 环绕通知
     * @return
     */
    protected Advice aroundAdvice(){
        return (MethodInterceptor) methodInvocation -> {
            //封装aop当前环境
            MethodInterceptorHolder methodInterceptorHolder = MethodInterceptorHolder.create(methodInvocation);

            //生成日志记录信息
            AccessLoggerInfo info = createLogger(methodInterceptorHolder);
            Object responseBody;
            try {
                eventPublisher.publishEvent(new AccessLoggerBeforeEvent(info));
                listeners.forEach(listener -> listener.onLogBefore(info));
                responseBody = methodInvocation.proceed();
                info.setResponse(responseBody);
            } catch (Throwable e) {
                info.setException(e);
                throw e;
            } finally {
                info.setResponseTime(System.currentTimeMillis());
                //设置响应状态
                HttpServletResponse httpResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                if (null != httpResponse) {
                    info.setHttpStatus(String.valueOf(httpResponse.getStatus()));
                }
                //触发监听
                eventPublisher.publishEvent(new AccessLoggerAfterEvent(info));
                listeners.forEach(listener -> listener.onLogger(info));
            }
            return responseBody;
        };
    }

    protected AccessLoggerInfo createLogger(MethodInterceptorHolder holder) {
        AccessLoggerInfo info = new AccessLoggerInfo();
        info.setId(MD5.create().digestHex(UUID.fastUUID() + RandomUtil.randomString(5)));
        info.setRequestTime(System.currentTimeMillis());

        LoggerDefine define = loggerParsers.stream()
                .filter(parser -> parser.support(ClassUtils.getUserClass(holder.getTarget()), holder.getMethod()))
                .findAny()
                .map(parser -> parser.parse(holder))
                .orElse(null);

        if (define != null) {
            info.setAction(define.getAction());
            info.setDescribe(define.getDescribe());
        }
        info.setParameters(holder.getArgs());
        info.setTarget(holder.getTarget().getClass());
        info.setMethod(holder.getMethod());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (null != request) {
            info.setHttpHeaders(ServletUtil.getHeaderMap(request));
            info.setUserAgent(ServletUtil.getHeader(request, "user-agent", StandardCharsets.UTF_8));
            info.setIp(ServletUtil.getClientIP(request));
            info.setHttpMethod(request.getMethod());
            info.setUrl(request.getRequestURL().toString());
        }
        return info;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return loggerParsers.stream().anyMatch(parser -> parser.support(aClass, method));
    }
}
