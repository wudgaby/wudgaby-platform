package com.wudgaby.starter.captcha.dao;

import com.wudgaby.starter.captcha.core.dao.CaptchaStoreDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 10:08
 * @desc :
 */
@Configuration
public class RedisCaptchaAutoConfiguration {
    @Bean
    public CaptchaStoreDao captchaStoreDao(StringRedisTemplate stringRedisTemplate){
        return new RedisCaptchaStoreDao(stringRedisTemplate);
    }

}
