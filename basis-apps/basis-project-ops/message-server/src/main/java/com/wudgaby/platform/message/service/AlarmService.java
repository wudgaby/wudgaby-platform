package com.wudgaby.platform.message.service;

import com.github.jaemon.dinger.DingerSender;
import com.github.jaemon.dinger.core.entity.DingerRequest;
import com.github.jaemon.dinger.core.entity.enums.MessageSubType;
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

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/7/10 16:58
 * @Desc :   
 */
@Slf4j
@Service
@AllArgsConstructor
public class AlarmService {
    private final AlarmProperties alarmConfiguration;
    private final MailService mailSendService;
    private final DingerSender dingerSender;

    @Async
    public void sendAlarmInfo(AlarmForm alarmForm) {
        String alarmContent = getAlarmContent(alarmForm);
        // 发送markdown类型消息
        dingerSender.send(
                MessageSubType.MARKDOWN,
                DingerRequest.request(alarmContent, alarmForm.getTitle())
        );

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
