package com.wudgaby.mail.springboot.demo;

import com.google.common.collect.Lists;
import com.wudgaby.mail.starter.service.MailReceiveService;
import com.wudgaby.mail.starter.service.MailSendService;
import com.wudgaby.mail.starter.vo.EmailMessage;
import com.wudgaby.mail.starter.vo.MailAccount;
import com.wudgaby.mail.starter.vo.MailQueryForm;
import com.wudgaby.mail.starter.vo.SendMailContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailDemoApplicationTests {
	@Autowired
	private MailProperties mailProperties;
	@Autowired
	private MailSendService mailSendService;
	@Autowired
	private MailReceiveService mailReceiveService;

	@Test
	public void contextLoads() {

	}

	@Test
	public void sendMailTest(){
		mailSendService.sendSimpleMail(new SendMailContent(new MailAccount(mailProperties.getUsername(), "123"), new String[]{"wudgaby@sina.com"}, "wo shi hkbird !你好", "测试看能发送吗"));
	}

	@Test
	public void receiveMailTest(){
		List<EmailMessage> list;
		//mailReceiveService.getMsgs(new MailAccount("all@wudgaby.live", "mymail123123"));
		//mailReceiveService.getEmailMessageList(new MailQueryForm(new MailAccount("wudgaby@mail.ru", "ABab147258")));
		//mailReceiveService.getEmailMessageList(new MailQueryForm(new MailAccount("wudgabymail@sina.com", "da471b968abd1c63")));
		//List<EmailMessage> list =  mailReceiveService.getEmailMessageList(new MailQueryForm(new MailAccount("wudgabymail@163.com", "TNGCCQFJCUUOGPKR")));
		//System.out.println(list.size());

		MailAccount mailAccount = new MailAccount("Ozie.Nee15523@abc.com", "5489AYHT5s");
		MailQueryForm mailQueryForm = new MailQueryForm(mailAccount)
				//.setMailFlag(Flags.Flag.RECENT)
				//.setSeen(false)
				//.setFlagSeen(false)
				.setAcceptContent(false)
				.setAcceptHtmlContent(false)
				.setFolders(Lists.newArrayList("\\w*"))
				;
		list =  mailReceiveService.getEmailMessageList(mailQueryForm);
		System.out.println(list.size());
		for(EmailMessage message : list){
			System.out.println(message.getSubject());
		}
	}
}
