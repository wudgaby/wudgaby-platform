package com.wudgaby.starter.captcha.core;

import java.io.OutputStream;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/9/28 0028 14:15
 * @desc :  验证码存储接口
 */
public interface CaptchaGenerateService {
    String generate();
    void generate(OutputStream os);
}
