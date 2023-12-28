package com.wudgaby.mail.starter.config;

import com.wudgaby.mail.starter.service.MailSendService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/4/13/013 14:10
 * @Desc :   
 */
@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailConfiguration {

    @Bean
    @ConditionalOnBean(JavaMailSender.class)
    @ConditionalOnMissingBean(MailSendService.class)
    public MailSendService mailSendService(JavaMailSender javaMailSender, DefaultMailSendProperties mailSendProperties){
        return new MailSendService(javaMailSender, mailSendProperties);
    }
}
