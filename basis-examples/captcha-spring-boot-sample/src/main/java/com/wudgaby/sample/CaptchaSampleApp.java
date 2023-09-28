package com.wudgaby.sample;

import cn.hutool.core.util.StrUtil;
import com.wudgaby.starter.captcha.config.CaptchaProp;
import com.wudgaby.starter.captcha.core.dao.CaptchaStoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 11:54
 * @desc :
 */
@SpringBootApplication
@RestController
public class CaptchaSampleApp {
    public static void main(String[] args) {
        SpringApplication.run(CaptchaSampleApp.class, args);
    }

    @Autowired
    private CaptchaStoreDao captchaStoreDao;
    @Autowired
    private CaptchaProp captchaProp;

    @GetMapping("/verify")
    public String getCaptChaResult(String key, String captcha){
        String code = captchaStoreDao.get(captchaProp.getCaptchaStorePrefixKey(), key).orElse(null);
        captchaStoreDao.clear(captchaProp.getCaptchaStorePrefixKey(), key);

        if(StrUtil.isBlank(code)){
            return "验证码已过期";
        }
        boolean isOK = Objects.equals(captcha, code);
        return code + " : " + (isOK ? "验证成功" : "验证失败");
    }

    @GetMapping("/filter")
    public String filterTest(String k, String code){
        return "已进入";
    }
}
