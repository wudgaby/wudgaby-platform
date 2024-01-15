package com.wudgaby.starter.access.limit;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/2 0002 10:43
 * @desc :  访问限制
 */
@Slf4j
@Aspect
public class AccessLimitAspect {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 锁住时的key前缀
     */
    public static final String LOCK_PREFIX = "ACCESS-LIMIT:LOCK:";

    /**
     * 统计次数时的key前缀
     */
    public static final String COUNT_PREFIX = "ACCESS-LIMIT:COUNT:";

    @Before("@within(accessLimit) || @annotation(accessLimit)")
    public void before(JoinPoint joinPoint, AccessLimit accessLimit){
        accessLimit(joinPoint);
    }

    private void accessLimit(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();


        String ip = ServletUtil.getClientIP(request);
        String uri = request.getMethod() + request.getServletPath();
        long second = 0L;
        long maxTime = 0L;
        long forbiddenTime = 0L;
        String msg = "";

        AccessLimit classAccessLimit = method.getDeclaringClass().getAnnotation(AccessLimit.class);
        if(classAccessLimit != null){
            second = classAccessLimit.second();
            maxTime = classAccessLimit.maxTime();
            forbiddenTime = classAccessLimit.forbiddenTime();
            msg = classAccessLimit.value();
        }

        //优先方法上的
        AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
        if(accessLimit != null){
            second = accessLimit.second();
            maxTime = accessLimit.maxTime();
            forbiddenTime = accessLimit.forbiddenTime();
            msg = accessLimit.value();
        }

        if(second <= 0 || maxTime <= 0 || forbiddenTime <= 0){
            return;
        }

        if (isForbidForRedis(second, maxTime, forbiddenTime, ip, uri)) {
            throw new AccessLimitException(msg);
        }
    }

    /**
     * 判断某用户访问某接口是否已经被禁用/是否需要禁用
     *
     * @param second        多长时间  单位/秒
     * @param maxTime       最大访问次数
     * @param forbiddenTime 禁用时长 单位/秒
     * @param ip            访问者ip地址
     * @param uri           访问的uri
     * @return ture为需要禁用
     */
    private boolean isForbidForRedis(long second, long maxTime, long forbiddenTime, String ip, String uri) {
        String ipBase64 = Base64.getEncoder().encodeToString(ip.getBytes());
        String lockKey = LOCK_PREFIX + ipBase64 + uri; //如果此ip访问此uri被禁用时的存在Redis中的 key
        Object isLock = redisTemplate.opsForValue().get(lockKey);
        // 判断此ip用户访问此接口是否已经被禁用
        if (Objects.isNull(isLock)) {
            // 还未被禁用
            String countKey = COUNT_PREFIX + ipBase64 + uri;
            Object count = redisTemplate.opsForValue().get(countKey);
            if (Objects.isNull(count)) {
                log.info("{} = 首次访问", countKey);
                redisTemplate.opsForValue().set(countKey, 1, second, TimeUnit.SECONDS);
            } else {
                // 此用户前一点时间就访问过该接口，且频率没超过设置
                if ((Integer) count < maxTime) {
                    redisTemplate.opsForValue().increment(countKey);
                } else {
                    log.info("{} 禁用访问 {}", ip, uri);
                    // 禁用
                    redisTemplate.opsForValue().set(lockKey, 1, forbiddenTime, TimeUnit.SECONDS);
                    // 删除统计--已经禁用了就没必要存在了
                    redisTemplate.delete(countKey);
                    return true;
                }
            }
        } else {
            // 此用户访问此接口已被禁用
            return true;
        }
        return false;
    }
}
