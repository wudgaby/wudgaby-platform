package com.wudgaby.plugin.resubmit;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wudgaby.redis.api.RedisSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
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
    private static final String KEY_REPEAT = "repeat";
    /**
     * key 过期时间
     */
    private static final long REPEAT_SUBMIT_EXPIRE_SEC = 2;
    /**
     * 前端传过来的时间戳
     */
    private static final String PARAM_TIMESTAMP = "t";

    private final RedisSupport redisSupport;

    private final LoginUserService loginUserService;

    private static final Cache<String, String> CACHE = CacheBuilder.newBuilder()
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

        RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);
        String message = Optional.ofNullable(repeatSubmit).map(RepeatSubmit::value).orElse("请勿重复提交.");
        long expires = Optional.ofNullable(repeatSubmit).map(r -> r.timeUnit().toSeconds(r.expires())).orElse(REPEAT_SUBMIT_EXPIRE_SEC);
        long expiresSec = Math.max(expires, 1);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (requestAttributes != null) {
            request = requestAttributes.getRequest();
        }
        Assert.notNull(request, "request can not null");
        String key = getKey(request, joinPoint);

        log.info("重复提交监测. key = [{}]", key);
        if(redisSupport != null){
            redisReSubmit(key, message, expiresSec);
        }else{
            guavaReSubmit(key, message);
        }
    }

    private void redisReSubmit(String key, String message, long expiresSec){
        Object val = redisSupport.get(key);
        if (val == null) {
            redisSupport.set(key, key, expiresSec);
        } else {
            log.warn("重复提交. key = [{}]", key);
            throw new ResubmitException(message);
        }
    }

    private void guavaReSubmit(String key, String message){
        String val = CACHE.getIfPresent(key);
        if (val == null) {
            CACHE.put(key, key);
        } else {
            log.warn("重复提交. key = [{}]", key);
            throw new ResubmitException(message);
        }
    }

    private String getKey(HttpServletRequest request, JoinPoint joinPoint) {
        String userId = Optional.ofNullable(loginUserService).map(LoginUserService::getLoggedUserId).orElse("anyone");
        String path = Base64.getEncoder().encodeToString((request.getMethod() + " " + request.getServletPath()).getBytes());
        String timestamp = Optional.ofNullable(request.getParameter(PARAM_TIMESTAMP)).orElse("0");
        //String md5Param = getMd5Param(request);
        //String md5Query = MD5.create().digestHex(request.getQueryString());


        String ipBase64 = Base64.getEncoder().encodeToString(ServletUtil.getClientIP(request).getBytes());
        return new StringJoiner(":")
                .add(KEY_REPEAT)
                .add(userId)
                .add(ipBase64)
                .add(path)
                .add(timestamp)
                .toString();
    }

    private String getMd5Param(HttpServletRequest request){
        String contentType = request.getContentType();
        if(StringUtils.isNotBlank(contentType) && contentType.contains("multipart/form-data")){
            return "";
        }else{
            Map<String, Object> filterMap = request.getParameterMap().entrySet().stream()
                    .filter(entry -> entry.getValue() != null)
                    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), LinkedHashMap::putAll);

            try{
                return MD5.create().digestHex(JSONUtil.toJsonStr(filterMap));
            }catch (Exception e){
                log.error("日志记录,请求体序列化失败. {}", e.getMessage());
            }
        }
        return "";
    }
}
