package com.wudgaby.platform.auth.extend.authway.sms;

import com.wudgaby.platform.auth.exceptions.ValidateCodeException;
import com.wudgaby.redis.api.RedisSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/9/8 18:35
 * @Desc :
 */
@Service
public class DefaultCaptchaService implements CaptchaService{
    @Autowired
    private RedisSupport redisSupport;

    @Override
    public boolean sendCaptchaCode(String phone) {
        return true;
    }

    @Override
    public boolean verifyCaptchaCode(String phone, String captcha) {
        if (StringUtils.isEmpty(captcha)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        String codeInRedis = (String)redisSupport.get("smscode:" + phone);
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (!captcha.equalsIgnoreCase(codeInRedis)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        return true;
    }
}
