package com.wudgaby.platform.message.controller;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.message.api.enmus.SmsTmplEnum;
import com.wudgaby.platform.message.api.form.SmsForm;
import com.wudgaby.platform.message.api.form.SmsTmplForm;
import com.wudgaby.platform.message.service.SmsService;
import com.wudgaby.plugin.resubmit.UnCheckRepeatSubmit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : SmsController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 10:44
 * @Desc :   TODO
 */
@Slf4j
@Api(tags = "短信功能")
@RestController
@AllArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @ApiOperation("发送注册验证码")
    @PostMapping("/sms/captcha/register")
    @UnCheckRepeatSubmit
    public ApiResult sendRegisterCaptchaCode(@Validated @RequestBody SmsForm smsForm) {
        return smsService.sendSingleSms(smsForm.getPhoneNum(), SmsTmplEnum.REGISTER_TMPL, new String[]{smsForm.getCaptcha()});
    }

    @ApiOperation("发送修改密码验证码")
    @PostMapping("/sms/captcha/modifyPwd")
    @UnCheckRepeatSubmit
    public ApiResult sendModifyPwdCaptchaCode(@Validated @RequestBody SmsForm smsForm) {
        return smsService.sendSingleSms(smsForm.getPhoneNum(), SmsTmplEnum.UPDATE_PWD_TMPL, new String[]{smsForm.getCaptcha()});
    }

    @ApiOperation("发送模板短信")
    @PostMapping("/sms/tmpl")
    @UnCheckRepeatSubmit
    public ApiResult sendTmplSms(@Validated @RequestBody SmsTmplForm smsTmplForm) {
        return smsService.sendTmplSms(smsTmplForm);
    }
}
