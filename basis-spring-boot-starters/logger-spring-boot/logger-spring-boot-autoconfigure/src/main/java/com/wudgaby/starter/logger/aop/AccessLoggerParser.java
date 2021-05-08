package com.wudgaby.starter.logger.aop;


import com.wudgaby.logger.api.vo.LoggerDefine;
import com.wudgaby.platform.core.aop.MethodInterceptorHolder;

import java.lang.reflect.Method;

public interface AccessLoggerParser {
    boolean support(Class clazz, Method method);

    LoggerDefine parse(MethodInterceptorHolder holder);
}
