package com.wudgaby.starter.mail.service;

import com.wudgaby.starter.mail.config.DefaultMailReceiveProperties;
import com.wudgaby.starter.mail.receive.MailMessageParser;
import com.wudgaby.starter.mail.receive.MailReceiverHelper;
import com.wudgaby.starter.mail.support.SimpleAuthenticator;
import com.wudgaby.starter.mail.util.MailUtil;
import com.wudgaby.starter.mail.vo.EmailMessage;
import com.wudgaby.starter.mail.vo.MailQueryForm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.Authenticator;
import javax.mail.Message;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/4/12/012 17:36
 * @Desc :
 */
@Slf4j
@Service
@AllArgsConstructor
public class MailReceiveService {
    private final DefaultMailReceiveProperties mailReceiveProperties;
    private final MailMessageParser messageParser;
    private final MailReceiverHelper receiverHelper;

    public List<EmailMessage> getEmailMessageList(MailQueryForm queryForm){
        Assert.notNull(queryForm, "查询对象不能为空");
        Assert.notNull(queryForm.getMailAccount(), "账号不能为空");

        Properties mailProperties = MailUtil.getMailPropertiesByEmail(queryForm.getMailAccount().getUserName(), mailReceiveProperties.getSupportMailList());
        Assert.notNull(mailProperties, "不支持的邮箱服务, "+ queryForm.getMailAccount().getUserName());

        log.info("获取 {} 的邮件.", queryForm.getMailAccount().getUserName());

        Authenticator authenticator = new SimpleAuthenticator(queryForm.getMailAccount().getUserName(), queryForm.getMailAccount().getPassword());

        Collection<Message> messageList;
        if(queryForm.getEachProtocol() != null && queryForm.getEachProtocol()){
            messageList = receiverHelper.fetchInboxEachProtocol(mailProperties, authenticator, queryForm);
        }else{
            messageList = receiverHelper.fetchInbox(mailProperties, authenticator, queryForm);
        }
        return messageParser.filterMessages(queryForm, messageList);
    }

    /**
     * 读取列表解析内容时, 已经自动设置为已读状态.
     * @param message
     */
    public void setSeen(Message message){
        Assert.notNull(message, "邮件信息不能为空");
        messageParser.flagRead(message);
    }
}
