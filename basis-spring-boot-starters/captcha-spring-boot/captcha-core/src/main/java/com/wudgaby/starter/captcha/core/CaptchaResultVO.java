package com.wudgaby.starter.captcha.core;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CaptchaResultVO {
    private String key;

    /**
     * base64图片
     */
    private String captcha;
}
