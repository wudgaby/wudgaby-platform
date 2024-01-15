package com.wudgaby.starter.logger.aop;

import com.wudgaby.logger.api.annotation.AccessLogger;
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
        boolean isSupport = api != null || operation != null;

        /**
         * 为了使用AccessLogger ignore 过滤不需要日志记录的接口
         */
        boolean isSupport2 = true;
        AccessLogger ann = AnnotationUtils.findAnnotation(method, AccessLogger.class);
        if(null != ann && ann.ignore()) {
            isSupport2 = false;
        }
        return isSupport && isSupport2;
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
