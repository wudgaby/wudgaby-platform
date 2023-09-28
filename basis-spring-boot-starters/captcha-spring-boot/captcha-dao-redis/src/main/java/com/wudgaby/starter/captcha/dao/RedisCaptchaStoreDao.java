package com.wudgaby.starter.captcha.dao;

import cn.hutool.core.lang.UUID;
import com.wudgaby.starter.captcha.core.CaptchaStoreDao;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 14:48
 * @desc :
 */
@AllArgsConstructor
public class RedisCaptchaStoreDao implements CaptchaStoreDao {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public String save(String prefix, String data) {
        return save(prefix, data, 600);
    }

    @Override
    public String save(String prefix, String data, long expire) {
        String key = UUID.fastUUID().toString(true);
        stringRedisTemplate.opsForValue().set(prefix + ":" + key, data, expire <= 0 ? 600 : expire, TimeUnit.SECONDS);
        return key;
    }

    @Override
    public Optional<String> get(String prefix, String key) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(prefix + ":" + key));
    }

    @Override
    public void clear(String prefix, String key) {
        stringRedisTemplate.delete(prefix + ":" + key);
    }
}
