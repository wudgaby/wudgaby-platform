package com.wudgaby.plugin.resubmit;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
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

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/11/14 18:23
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
    private static final long REPEAT_SUBMIT_EXPIRE_SEC = 1;
    /**
     * 前端传过来的时间戳
     */
    private static final String PARAM_TIMESTAMP = "t";

    private final ResubmitService resubmitService;

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
        resubmitService.check(key, message, expiresSec);
    }

    private String getKey(HttpServletRequest request, JoinPoint joinPoint) {
        String userId = Optional.ofNullable(resubmitService).map(ResubmitService::getLoggedUserId).orElse("anyone");
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
