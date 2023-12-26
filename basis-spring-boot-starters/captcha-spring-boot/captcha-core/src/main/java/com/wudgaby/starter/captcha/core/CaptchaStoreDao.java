package com.wudgaby.starter.captcha.core;

import java.util.Optional;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 14:15
 * @desc :  验证码存储接口
 */
public interface CaptchaStoreDao {
    String save(String prefix, String data);
    String save(String prefix, String data, long expire);
    Optional<String> get(String prefix, String key);
    void clear(String prefix, String key);
}
