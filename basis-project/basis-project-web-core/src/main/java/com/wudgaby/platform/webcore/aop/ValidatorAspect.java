package com.wudgaby.platform.webcore.aop;

import com.wudgaby.platform.core.exception.ValidatorFormException;
import com.wudgaby.platform.core.support.CustomFormValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/24/024 1:03
 * @Desc : controller验证标记
 */

@Slf4j
@Aspect
@Order(1)
@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "validator.enabled", havingValue = "true")
public class ValidatorAspect {
    private final CustomFormValidator customFormValidator;

    public void pointcut(){}

    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(mapping)")
    public void before(JoinPoint joinPoint, RequestMapping mapping) throws Throwable {
        paramValid(joinPoint);
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(mapping)")
    public void before(JoinPoint joinPoint, GetMapping mapping) throws Throwable {
        paramValid(joinPoint);
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(mapping)")
    public void before(JoinPoint joinPoint, PostMapping mapping) throws Throwable {
        paramValid(joinPoint);
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) &&  @annotation(mapping)")
    public void before(JoinPoint joinPoint, PutMapping mapping) throws Throwable {
        paramValid(joinPoint);
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(mapping)")
    public void before(JoinPoint joinPoint, DeleteMapping mapping) throws Throwable {
        paramValid(joinPoint);
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(mapping)")
    public void before(JoinPoint joinPoint, PatchMapping mapping) throws Throwable {
        paramValid(joinPoint);
    }

    private void paramValid(JoinPoint joinPoint) throws Throwable {
        log.info("[PARAM VALID]");
        // 获得切入方法参数
        Object [] args = joinPoint.getArgs();

        Map<String, Object> paramErrorMap = customFormValidator.validate(args);
        if(!paramErrorMap.isEmpty()){
            throw new ValidatorFormException("请求参数校验错误", paramErrorMap);
        }
    }
}
