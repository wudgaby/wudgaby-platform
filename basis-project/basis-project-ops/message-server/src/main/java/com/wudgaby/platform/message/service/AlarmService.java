package com.wudgaby.platform.message.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.message.api.form.AlarmForm;
import com.wudgaby.platform.message.api.form.NormalMailForm;
import com.wudgaby.platform.message.config.properties.AlarmProperties;
import com.wudgaby.platform.utils.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @ClassName : AlarmService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 16:58
 * @Desc :   TODO
 */
@Slf4j
@Service
@AllArgsConstructor
public class AlarmService {
    private final AlarmProperties alarmConfiguration;
    private final MailSendService mailSendService;

    @Async
    public void sendAlarmInfo(AlarmForm alarmForm) {
        DingTalkClient client = new DefaultDingTalkClient(alarmConfiguration.getDdWebhook());
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(alarmForm.getTitle());
        //内容
        String alarmContent = getAlarmContent(alarmForm);
        markdown.setText(alarmContent);
        request.setMarkdown(markdown);
        try {
            client.execute(request);
            log.info("send to dingding success.");
        } catch (ApiException e) {
            throw new BusinessException(ApiResult.failure().getCode(), e.getMessage());
        }

        //是否发送到邮件
        if (alarmConfiguration.isSendMail()) {
            NormalMailForm normalMailForm = new NormalMailForm();
            normalMailForm.setSubject(alarmForm.getTitle());
            normalMailForm.setSendTo(alarmConfiguration.getSendTo());
            normalMailForm.setContent(alarmContent);

            mailSendService.sendHTMLMail(normalMailForm);
        }
    }

    private String getAlarmContent(AlarmForm alarmForm) {
        StringBuilder sb = new StringBuilder();
        sb.append("### 【监控告警】 \n");
        sb.append("- 服务: ").append(alarmForm.getServiceName()).append("<br/>\n");
        sb.append("- IP: ").append(alarmForm.getIp()).append("<br/>\n");
        sb.append("- 级别: ").append(Optional.ofNullable(alarmForm.getLevel()).map(l -> l.getDesc()).orElse("无")).append("<br/>\n");
        sb.append("- 类型: ").append(Optional.ofNullable(alarmForm.getEventType()).map(l -> l.getDesc()).orElse("无")).append("<br/>\n");
        sb.append("- 平台: ").append(alarmForm.getPlatform()).append("<br/>\n");
        sb.append("- 发生时间: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(LocalDateTimeUtil.COMMON_DATETIME))).append("<br/>\n");
        sb.append("- 描述: ").append(alarmForm.getContent()).append("<br/>\n");
        return sb.toString();
    }
}
