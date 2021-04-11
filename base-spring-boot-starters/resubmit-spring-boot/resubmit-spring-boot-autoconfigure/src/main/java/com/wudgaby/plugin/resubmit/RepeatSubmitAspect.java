package com.wudgaby.plugin.resubmit;

import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wudgaby.redis.api.RedisSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : RepeatSubmitAspect
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/14 18:23
 * @Desc : RepeatSubmit 或 PostMapping 处理重复提交
 * 不可同时标记,会处理两次
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class RepeatSubmitAspect {
    private static final String KEY_REPEAT = "form:repeat";
    /**
     * key 过期时间
     */
    private static final long REPEAT_SUBMIT_EXPIRE_SEC = 2;
    /**
     * 前端传过来的时间戳
     */
    private static final String PARAM_TIMESTAMP = "t";

    private final RedisSupport redisSupport;

    private static final Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(REPEAT_SUBMIT_EXPIRE_SEC, TimeUnit.SECONDS)
            .maximumSize(99_999)
            .build();

    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(repeatSubmit)")
    public void before(JoinPoint joinPoint, RepeatSubmit repeatSubmit){
        repeatSubmit(joinPoint);
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(mapping)")
    public void before(JoinPoint joinPoint, PostMapping mapping){
        repeatSubmit(joinPoint);
    }

    private void repeatSubmit(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        UnCheckRepeatSubmit unCheckRepeatSubmit = method.getAnnotation(UnCheckRepeatSubmit.class);
        if(unCheckRepeatSubmit != null){
            return;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Assert.notNull(request, "request can not null");
        String key = getKey(request, joinPoint);

        log.info("重复提交监测. key = [{}]", key);
        if(redisSupport != null){
            redisReSubmit(key);
        }else{
            guavaReSubmit(key);
        }
    }

    private void redisReSubmit(String key){
        Object val = redisSupport.get(key);
        if (val == null) {
            redisSupport.set(key, key, REPEAT_SUBMIT_EXPIRE_SEC);
        } else {
            log.warn("重复提交. key = [{}]", key);
            throw new ResubmitException();
        }
    }

    private void guavaReSubmit(String key){
        String val = cache.getIfPresent(key);
        if (val == null) {
            cache.put(key, key);
        } else {
            log.warn("重复提交. key = [{}]", key);
            throw new ResubmitException();
        }
    }

    private String getKey(HttpServletRequest request, JoinPoint joinPoint) {
        //String account = SecurityUtils.getCurrentUser().map(u -> u.getAccount()).orElse("guest");
        String userId = "";
        String path = request.getServletPath();
        //String url = request.getRequestURL().toString();
        //String argString = Arrays.asList(joinPoint.getArgs()).toString();
        String timestamp = Optional.ofNullable(request.getParameter(PARAM_TIMESTAMP)).orElse("0");

        String ipBase64 = Base64.getEncoder().encodeToString(ServletUtil.getClientIP(request).getBytes());
        return new StringJoiner(":")
                .add(KEY_REPEAT)
                .add(ipBase64)
                .add(userId)
                .add(path)
                //.add(argString)
                .add(timestamp)
                .toString();
    }
}
