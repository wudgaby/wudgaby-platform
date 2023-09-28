package com.wudgaby.starter.captcha.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName : CaptChaCheckAspect
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2023.09.28
 * @Desc :
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class CaptChaCheckAspect {

    @Before("@annotation(captChaCheck)")
    public void before(JoinPoint joinPoint, CaptChaCheck captChaCheck){
        check(joinPoint);
    }

    @Before("@annotation(mapping)")
    public void before(JoinPoint joinPoint, PostMapping mapping){
        check(joinPoint);
    }

    private void check(JoinPoint joinPoint){

    }

}
