package com.wudgaby.platform.message.service;

import com.google.common.collect.Maps;
import com.wudgaby.mail.starter.service.MailSendService;
import com.wudgaby.mail.starter.vo.MailAccount;
import com.wudgaby.mail.starter.vo.SendMailContent;
import com.wudgaby.platform.message.api.form.MailTmplForm;
import com.wudgaby.platform.message.api.form.NormalMailForm;
import com.wudgaby.platform.message.constants.SystemConstants;
import com.wudgaby.platform.message.exception.MailServiceException;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/4/12/012 17:36
 * @Desc :
 */
@Slf4j
@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private MailSendService mailSendService;



    /**
     * 发送普通邮件
     *
     * @param mailForm
     */
    @Async
    public void sendSimpleMail(NormalMailForm mailForm) {
        SendMailContent sendMailContent = new SendMailContent(new MailAccount(from, ""),
                mailForm.getSendTo(),
                mailForm.getSubject(),
                mailForm.getContent());
        mailSendService.sendSimpleMail(sendMailContent);
    }

    /**
     * 发送html邮件
     *
     * @param mailForm
     */
    @Async
    public void sendHTMLMail(NormalMailForm mailForm) {
        SendMailContent sendMailContent = new SendMailContent(new MailAccount(from, ""),
                mailForm.getSendTo(),
                mailForm.getSubject(),
                mailForm.getContent());
        mailSendService.sendHTMLMail(sendMailContent);
    }

    /**
     * 发送模板邮件
     * 模板内变量名称规则 p0,p1,p2,p3...
     *
     * @param mailTmplForm
     */
    @Async
    public void sendTmplMail(MailTmplForm mailTmplForm) {
        try {
            NormalMailForm mailForm = new NormalMailForm();
            mailForm.setSendTo(mailTmplForm.getSendTo());
            mailForm.setSubject(mailTmplForm.getMailTmplEnum().getSubject());

            Map<String, Object> paramMap = Maps.newHashMap();
            if (ArrayUtils.isNotEmpty(mailTmplForm.getParams())) {
                for (int i = 0; i < mailTmplForm.getParams().length; i++) {
                    paramMap.put("p" + i, mailTmplForm.getParams()[i]);
                }
            }
            mailForm.setContent(getTmplContent(mailTmplForm.getMailTmplEnum().getTmplFileName(), paramMap));
            sendHTMLMail(mailForm);
        } catch (Exception e) {
            throw new MailServiceException("模板邮件发送失败!" + e.getMessage(), e);
        }
    }

    /**
     * 解析模板
     *
     * @param fileName
     * @param params
     * @return
     */
    private String getTmplContent(String fileName, Map<String, Object> params) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(this.getClass(), SystemConstants.TEMPLATES_PATH);

        return FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(fileName), params);
    }
}
