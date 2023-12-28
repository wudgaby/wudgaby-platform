package com.wudgaby.starter.captcha.interceptor;

import cn.hutool.core.util.StrUtil;
import com.wudgaby.starter.captcha.config.CaptchaProp;
import com.wudgaby.starter.captcha.core.CaptChaCheck;
import com.wudgaby.starter.captcha.core.CaptchaException;
import com.wudgaby.starter.captcha.core.CaptchaStoreDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2023.09.28
 * @Desc :
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class CaptChaCheckAspect {
    private final CaptchaProp captchaProp;
    private final CaptchaStoreDao captchaStoreDao;

    @Before("@annotation(captChaCheck)")
    public void before(JoinPoint joinPoint, CaptChaCheck captChaCheck){
        check(joinPoint);
    }

    private void check(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        try {
            validate(request);
        }catch (CaptchaException captchaException) {
            throw captchaException;
        } catch (Exception ex){
            throw new RuntimeException("系统异常");
        }
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        String key = ServletRequestUtils.getStringParameter(request, captchaProp.getAutoCheckMode().getKeyName());
        String captchaInRequest = ServletRequestUtils.getStringParameter(request, captchaProp.getAutoCheckMode().getCaptchaName());

        if (StrUtil.isBlank(captchaInRequest)) {
            throw new CaptchaException("请填写验证码!");
        }
        String captchaInStore = captchaStoreDao.get(captchaProp.getStorePrefixKey(), key).orElse(null);
        if (StrUtil.isBlank(captchaInStore)) {
            throw new CaptchaException("验证码错误!");
        }
        if (!StrUtil.equalsIgnoreCase(captchaInRequest, captchaInStore)) {
            throw new CaptchaException("验证码错误!");
        }
        //验证通过 移除缓存
        captchaStoreDao.clear(captchaProp.getStorePrefixKey(), key);
    }

}
