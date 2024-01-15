package com.wudgaby.starter.mail.service;

import com.wudgaby.starter.mail.config.DefaultMailSendProperties;
import com.wudgaby.starter.mail.exception.MailServiceException;
import com.wudgaby.starter.mail.support.SimpleAuthenticator;
import com.wudgaby.starter.mail.vo.MailAccount;
import com.wudgaby.starter.mail.vo.SendMailContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Date;
import java.util.Properties;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/4/12/012 17:36
 * @Desc :
 */
@Slf4j
@AllArgsConstructor
public class MailSendService {
    private final JavaMailSender javaMailSender;
    private final DefaultMailSendProperties mailSendProperties;

    /**
     * 发送普通邮件
     * @param sendMailContent
     */
    public void sendSimpleMail(SendMailContent sendMailContent){
        sendCustomSimpleMail(sendMailContent, this.javaMailSender);
    }

    public void sendCustomSimpleMail(SendMailContent sendMailContent, JavaMailSender javaMailSender){
        checkSendMailParam(sendMailContent);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //邮件发送人
        simpleMailMessage.setFrom(sendMailContent.getMailAccount().getUserName());
        //邮件接收人
        simpleMailMessage.setTo(sendMailContent.getSendTo());
        simpleMailMessage.setCc(sendMailContent.getCcTos());
        //邮件主题
        simpleMailMessage.setSubject(sendMailContent.getSubject());
        //邮件内容
        simpleMailMessage.setText(sendMailContent.getContent());

        try{
            javaMailSender.send(simpleMailMessage);
            log.info("send mail success.from: <{}>", sendMailContent.getMailAccount().getUserName());
        }catch(MailException e){
            log.error(e.getMessage(), e);
            throw new MailServiceException("邮件发送失败!" + e.getMessage(), e);
        }
    }

    /**
     * 发送html邮件
     * @param sendMailContent
     */
    public void sendHTMLMail(SendMailContent sendMailContent) {
        checkSendMailParam(sendMailContent);

        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(sendMailContent.getMailAccount().getUserName());
            mimeMessageHelper.setTo(sendMailContent.getSendTo());
            mimeMessageHelper.setCc(sendMailContent.getCcTos());
            mimeMessageHelper.setSubject(sendMailContent.getSubject());
            mimeMessageHelper.setText(sendMailContent.getContent(), true);

            if(mailSendProperties.isPretendSendHeader()){
                //防止成为垃圾邮件，披上outlook的马甲
                mimeMailMessage.setHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
            }

            javaMailSender.send(mimeMailMessage);
            log.info("send mail success.from: <{}>", sendMailContent.getMailAccount().getUserName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MailServiceException("邮件发送失败!" + e.getMessage(), e);
        }
    }

    /**
     * 发送附件
     * @param sendMailContent
     */
    public void sendAttachmentMail(SendMailContent sendMailContent) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(sendMailContent.getMailAccount().getUserName());
            mimeMessageHelper.setTo(sendMailContent.getSendTo());
            mimeMessageHelper.setCc(sendMailContent.getCcTos());
            mimeMessageHelper.setSubject(sendMailContent.getSubject());
            mimeMessageHelper.setText(sendMailContent.getContent());

            if(mailSendProperties.isPretendSendHeader()){
                //防止成为垃圾邮件，披上outlook的马甲
                mimeMailMessage.setHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
            }

            if(CollectionUtils.isEmpty(sendMailContent.getAttachmentPathList())){
                throw new MailServiceException("邮件发送失败!");
            }

            sendMailContent.getAttachmentPathList().forEach(path ->{
                //文件路径
                FileSystemResource file = new FileSystemResource(new File(path));
                try {
                    mimeMessageHelper.addAttachment("attachment", file);
                } catch (MessagingException e) {
                    throw new MailServiceException(e.getMessage(), e);
                }
            });

            javaMailSender.send(mimeMailMessage);
            log.info("send mail success.from: <{}>", sendMailContent.getMailAccount().getUserName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MailServiceException("邮件发送失败!" + e.getMessage(), e);
        }
    }

    /**
     * 发送邮件
     * @param sendMailContent
     */
    public void sendMail(SendMailContent sendMailContent){
        checkSendMailParam(sendMailContent);

        log.info("try send mail.from: <{}>", sendMailContent.getMailAccount().getUserName());
        try {
            Properties properties = new Properties();
            Message mimeMailMessage = new MimeMessage(getSession(sendMailContent.getMailAccount(), properties));
            mimeMailMessage.setFrom(new InternetAddress(sendMailContent.getMailAccount().getUserName()));
            mimeMailMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(StringUtils.join(sendMailContent.getSendTo(), ",")));

            //转发
            if(ArrayUtils.isNotEmpty(sendMailContent.getCcTos())){
                mimeMailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(StringUtils.join(sendMailContent.getCcTos(), ",")));
            }
            mimeMailMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
            mimeMailMessage.setSubject(sendMailContent.getSubject());
            mimeMailMessage.setSentDate(new Date());

            if(mailSendProperties.isPretendSendHeader()){
                //防止成为垃圾邮件，披上outlook的马甲
                mimeMailMessage.setHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");
            }

            BodyPart mbodyPart = new MimeBodyPart();
            mbodyPart.setContent(sendMailContent.getContent(), "text/html; charset=UTF-8");
            Multipart mm = new MimeMultipart();
            mm.addBodyPart(mbodyPart);
            mimeMailMessage.setContent(mm);

            Transport.send(mimeMailMessage);
            log.info("send mail success.from: <{}>", sendMailContent.getMailAccount().getUserName());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new MailServiceException("邮件发送失败!" + e.getMessage(), e);
        }
    }

    private void checkSendMailParam(SendMailContent sendMailContent){
        Assert.notNull(sendMailContent, "发送邮件对象不能为空");
        Assert.notNull(sendMailContent.getMailAccount(), "邮箱账号不能为空");
        Assert.noNullElements(sendMailContent.getSendTo(), "接收账号不能为空");
        Assert.hasText(sendMailContent.getSubject(), "邮件标题不能为空");
        Assert.hasText(sendMailContent.getContent(), "邮件内容不能为空");
    }

    /**
     * 登录 获得一个会话
     * @param mailAcc
     * @param props
     * @return
     */
    private static Session getSession(MailAccount mailAcc, Properties props){
        return Session.getInstance(props, new SimpleAuthenticator(mailAcc.getUserName(), mailAcc.getPassword()));
    }
}
