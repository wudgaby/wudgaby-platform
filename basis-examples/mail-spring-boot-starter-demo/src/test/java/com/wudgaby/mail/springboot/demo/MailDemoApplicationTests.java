package com.wudgaby.mail.springboot.demo;

import com.google.common.collect.Lists;
import com.wudgaby.starter.mail.service.MailReceiveService;
import com.wudgaby.starter.mail.service.MailSendService;
import com.wudgaby.starter.mail.vo.EmailMessage;
import com.wudgaby.starter.mail.vo.MailAccount;
import com.wudgaby.starter.mail.vo.MailQueryForm;
import com.wudgaby.starter.mail.vo.SendMailContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
		//mailSendService.sendSimpleMail(new SendMailContent(new MailAccount(mailProperties.getUsername(), "123"), new String[]{"wudgaby@sina.com"}, "wo shi hkbird !你好", "测试看能发送吗"));

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("192.168.98.141");
		sender.setPort(1026);
		sender.setUsername("hkbird@est.com");
		sender.setPassword("aaaaa");
		sender.setProtocol("smtp");
		sender.setDefaultEncoding("utf8");
		mailSendService.sendCustomSimpleMail(new SendMailContent(new MailAccount("hkbird@test.com", "test"), new String[]{"bar@example.com"}, "wo shi hkbird !你好", "测试看能发送吗"), sender);
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
