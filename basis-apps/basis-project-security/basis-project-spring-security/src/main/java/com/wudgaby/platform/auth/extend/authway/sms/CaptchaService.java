package com.wudgaby.platform.auth.extend.authway.sms;

/**
 * The interface Captcha service.
 *
 * @author felord.cn
 * @since 1.0.8.RELEASE
 */
public interface CaptchaService {
    /**
     * Send captcha code string.
     *
     * @param phone the phone
     * @return the boolean
     */
    boolean sendCaptchaCode(String phone);

    /**
     * 根据手机号去缓存中获取验证码同{@code captcha}进行对比，对比成功从缓存中主动清除验证码
     *
     * @param phone   手机号
     * @param captcha 前端传递的验证码
     * @return the boolean
     */
    boolean verifyCaptchaCode(String phone,String captcha);
}
