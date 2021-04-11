package com.wudgaby.locks.starter.aspect;

import com.wudgaby.locks.starter.api.annotation.DistributedLock;
import com.wudgaby.locks.starter.api.enums.LockType;
import com.wudgaby.locks.starter.api.exception.LockException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : LockAspect
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/25 15:09
 * @Desc :   TODO
 */
@Slf4j
@Aspect
@Order(1)
@AllArgsConstructor
public class LockAspect {
    private static final String REDISSON_LOCK_PREFIX = "redisson:lock:";

    private final RedissonClient redissonClient;

    /**
     * 用于SpEL表达式解析.
     */
    private static ExpressionParser parser;

    /**
     * 用于获取方法参数定义名字.
     */
    private static ParameterNameDiscoverer parameterNameDiscoverer;

    static {
        //创建SpEL表达式的解析器
        parser = new SpelExpressionParser();
        parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }


    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        if (ArrayUtils.isEmpty(distributedLock.keys())) {
            throw new LockException("keys不能为空");
        }
        boolean isMoreLock = distributedLock.lockType() == LockType.MULTI || distributedLock.lockType() == LockType.RED;
        if (isMoreLock && distributedLock.keys().length <= 1) {
            throw new LockException("MULTI,RED 锁模式下, keys需要多个");
        }
        if (!isMoreLock && distributedLock.keys().length > 1) {
            throw new LockException("非 MULTI,RED 锁模式下, keys只能有1个.");
        }
        log.info("AOP ---> 锁类型: {},获取锁等待时间 {} 秒. 锁超时时间 {} 秒", distributedLock.lockType(), distributedLock.lockWait(), distributedLock.timeout());

        RLock lock = getLock(distributedLock.keys(), distributedLock.lockType(), joinPoint);
        if (lock == null) {
            throw new LockException("获取锁失败");
        }

        boolean res = false;
        try {
            if (distributedLock.lockWait() <= 0) {
                res = true;
                lock.lock(distributedLock.timeout(), distributedLock.timeUnit());
                log.info("{} 成功获取锁.", Thread.currentThread().getName());
                return joinPoint.proceed();
            } else {
                res = lock.tryLock(distributedLock.lockWait(), distributedLock.timeout(), distributedLock.timeUnit());
                if (res) {
                    log.info("{} 成功获取锁.", Thread.currentThread().getName());
                    return joinPoint.proceed();
                } else {
                    log.info("{} 获取锁失败!超时.", Thread.currentThread().getName());
                    throw new LockException("获取锁失败!超时.");
                }
            }
        } finally {
            if (res) {
                log.info("释放锁.");
                lock.unlock();
            }
        }
    }

    private RLock getLock(String[] keys, LockType lockType, ProceedingJoinPoint joinPoint) {
        RLock rLock = null;
        RReadWriteLock rwlock = null;

        switch (lockType) {
            case REENTRANT:
                rLock = redissonClient.getLock((getKeyBySpEL(keys[0], joinPoint)));
                break;
            case FAIR:
                rLock = redissonClient.getFairLock(getKeyBySpEL(keys[0], joinPoint));
                break;
            case RED:
                RLock[] locks = new RLock[keys.length];
                for (int i = 0; i < keys.length; i++) {
                    List<String> valueBySpel = getKeysBySpEL(keys, joinPoint);
                    for (String s : valueBySpel) {
                        locks[i] = redissonClient.getLock(s);
                    }
                }
                rLock = new RedissonRedLock(locks);
                break;
            case MULTI:
                locks = new RLock[keys.length];
                for (int i = 0; i < keys.length; i++) {
                    List<String> valueBySpel = getKeysBySpEL(keys, joinPoint);
                    for (String s : valueBySpel) {
                        locks[i] = redissonClient.getLock(s);
                    }
                }
                rLock = new RedissonMultiLock(locks);
                break;
            case READ:
                rwlock = redissonClient.getReadWriteLock((getKeyBySpEL(keys[0], joinPoint)));
                rLock = rwlock.readLock();
                break;
            case WRITE:
                rwlock = redissonClient.getReadWriteLock((getKeyBySpEL(keys[0], joinPoint)));
                rLock = rwlock.writeLock();
                break;
            default:
                throw new LockException("未知的锁类型: " + lockType);
        }

        return rLock;
    }

    private List<String> getKeysBySpEL(String[] keys, ProceedingJoinPoint joinPoint) {
        List<String> keyList = new ArrayList<>(keys.length);
        for (String key : keys) {
            keyList.add(getKeyBySpEL(key, joinPoint));
        }
        return keyList;
    }

    private String getKeyBySpEL(String key, ProceedingJoinPoint joinPoint) {
        if (StringUtils.isBlank(key)) {
            throw new LockException("锁key 不能为空");
        }

        if (!key.contains("#")) {
            return REDISSON_LOCK_PREFIX + key;
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(methodSignature.getMethod());
        Expression expression = parser.parseExpression(key);
        //解析表达式需要的上下文，解析时有一个默认的上下文
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        String result = expression.getValue(context).toString();
        log.info("spel表达式计算结果 key : {}, result : {}", key, result);
        return result;
    }
}
