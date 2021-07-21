package com.wudgaby.platform.message.controller;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.message.api.form.MailTmplForm;
import com.wudgaby.platform.message.api.form.NormalMailForm;
import com.wudgaby.platform.message.service.MailSendService;
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
 * @ClassName : MailController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 10:44
 * @Desc :   
 */
@Slf4j
@Api(tags = "邮件功能")
@RestController
@AllArgsConstructor
public class MailController {
    private final MailSendService mailSendService;

    @ApiOperation("发送普通消息")
    @PostMapping("/mail/normal")
    @UnCheckRepeatSubmit
    public ApiResult sendNormalMail(@Validated @RequestBody NormalMailForm mailForm) {
        if (mailForm.isHtml()) {
            mailSendService.sendHTMLMail(mailForm);
        } else {
            mailSendService.sendSimpleMail(mailForm);
        }
        return ApiResult.success();
    }

    @ApiOperation("发送模板消息")
    @PostMapping(value = "/mail/tmpl")
    @UnCheckRepeatSubmit
    public ApiResult sendTmplMail(@Validated @RequestBody MailTmplForm mailForm) {
        mailSendService.sendTmplMail(mailForm);
        return ApiResult.success();
    }
}
