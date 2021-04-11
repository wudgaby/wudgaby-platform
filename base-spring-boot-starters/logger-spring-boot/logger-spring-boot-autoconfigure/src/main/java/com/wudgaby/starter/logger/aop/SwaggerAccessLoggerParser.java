package com.wudgaby.starter.logger.aop;

import com.wudgaby.logger.api.vo.LoggerDefine;
import com.wudgaby.platform.core.aop.MethodInterceptorHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class SwaggerAccessLoggerParser implements AccessLoggerParser {
    @Override
    public boolean support(Class clazz, Method method) {
        Api api = AnnotationUtils.findAnnotation(clazz, Api.class);
        ApiOperation operation = AnnotationUtils.findAnnotation(method, ApiOperation.class);
        return api != null || operation != null;
    }

    @Override
    public LoggerDefine parse(MethodInterceptorHolder holder) {
        Api api = holder.findAnnotation(Api.class);
        ApiOperation operation = holder.findAnnotation(ApiOperation.class);
        String action = "";
        String desc = "";
        if (api != null) {
            action = action.concat(api.value());
        }
        if (null != operation) {
            action = StringUtils.isEmpty(action) ? operation.value() : action + "-" + operation.value();
            desc = operation.notes();
        }
        return new LoggerDefine(action, desc);
    }
}
