package com.wudgaby.platform.limiter.service;

import cn.hutool.extra.expression.ExpressionUtil;
import cn.hutool.extra.expression.engine.ExpressionFactory;
import cn.hutool.extra.expression.engine.spel.SpELEngine;
import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wudgaby.platform.limiter.core.LimitType;
import com.wudgaby.platform.limiter.core.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName : RateLimiterService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/30 20:56
 * @Desc :
 */
@Slf4j
public abstract class RateLimiterService {
    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    abstract public boolean isAllowed(RateLimiter rateLimiter, Method method, Object[] args);

    protected List<String> getKeyExpressionResultList(RateLimiter rateLimiter, Method method, Object[] args) throws Exception {
        List<String> evalResultList = Lists.newArrayList();
        List<String> keyExpressionList;
        LimitType limitType = rateLimiter.limitType();
        if(limitType == LimitType.IP){
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            keyExpressionList = Lists.newArrayList(ServletUtil.getClientIP(httpServletRequest));
        }else{
            keyExpressionList = Lists.newArrayList(rateLimiter.key());
            if (keyExpressionList.isEmpty()) {
                keyExpressionList.add("GLOBAL");
            }
        }

        String[] names = new String[0];
        if(method != null){
            names = nameDiscoverer.getParameterNames(method);
        }

        for(String keyExpress : keyExpressionList) {
            if (keyExpress.contains("${")) {
                Map<String, Object> params = Maps.newHashMapWithExpectedSize(8);
                for (int i = 0; i < args.length; i++) {
                    params.put(names.length > i ? names[i] : "arg" + i, args[i]);
                    params.put("arg" + i, args[i]);

                }

                String evalResult = ExpressionUtils.analytical(keyExpress, params, "spel");
                evalResultList.add(evalResult);
            }
        }
        return evalResultList;
    }
}
