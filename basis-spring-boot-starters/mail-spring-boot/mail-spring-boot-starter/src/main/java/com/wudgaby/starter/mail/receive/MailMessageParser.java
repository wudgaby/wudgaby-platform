package com.wudgaby.starter.mail.receive;

import com.google.common.collect.Lists;
import com.sun.mail.imap.IMAPMessage;
import com.wudgaby.starter.mail.config.DefaultMailReceiveProperties;
import com.wudgaby.starter.mail.vo.EmailMessage;
import com.wudgaby.starter.mail.vo.MailQueryForm;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
public class MailMessageParser {
	private final DefaultMailReceiveProperties mailReceiveProperties;

	@SneakyThrows
	public List<EmailMessage> filterMessages(MailQueryForm queryForm, Collection<Message> messageCollection) {
		log.info("读取来自 <{}> 的 已读:{} 的 {} 封邮件.",
				Optional.ofNullable(queryForm.getFrom()).orElse("All"),
				//Optional.ofNullable(queryForm.getMailFlag()).map(Flags.Flag::toString).orElse("All"),
				Optional.ofNullable(queryForm.getSeen()).map(String::valueOf).orElse("All"),
				Optional.ofNullable(queryForm.getReceiveNum()).map(String::valueOf).orElse("All"));

		List<EmailMessage> emailMessageList = Lists.newArrayList();
		List<Message> messageList = Lists.newArrayList(messageCollection);
		Collections.reverse(messageList);

		for (Message message : messageList){
			EmailMessage em = parseMessage(message, queryForm);
			log.info(em.toString());

			if(!em.getSeen() && Boolean.valueOf(Objects.toString(queryForm.getFlagSeen()))){
				flagRead(message);
			}

			if(StringUtils.isNotBlank(queryForm.getFrom()) && !queryForm.getFrom().equalsIgnoreCase(em.getFrom())){
				continue;
			}

			if(queryForm.getSeen() != null && !queryForm.getSeen().equals(em.getSeen())){
				continue;
			}

			emailMessageList.add(em);

			if(queryForm.getReceiveNum() != null && queryForm.getReceiveNum() > 0
					&& emailMessageList.size() >= queryForm.getReceiveNum()){
				break;
			}
		}
		return emailMessageList;
	}

	@SneakyThrows
	private EmailMessage parseMessage(Message message, MailQueryForm queryForm) {
		//log.info("message mintype:{}", message.getContentType());
		EmailMessage em;

		boolean isNew = isNewMail(message);
		boolean isSeen = isSeen(message);

		if(message instanceof IMAPMessage){
			em =  parseImapMessage((IMAPMessage)message, queryForm);
			em.setMessageId(((IMAPMessage) message).getMessageID());
		}else{
			em = parseMimeMessage((MimeMessage)message, queryForm);
			em.setMessageId(((MimeMessage) message).getMessageID());
		}
		em.setMessageNo(message.getMessageNumber());
		em.setNewMail(isNew);
		em.setSeen(isSeen);
		return em;
	}

	@SneakyThrows
	private EmailMessage parseImapMessage(IMAPMessage message, MailQueryForm queryForm){
		//log.info("parse imap message");
		String subject = message.getSubject();
		String from = "";
		if(ArrayUtils.isNotEmpty(message.getFrom())){
			from = ((InternetAddress)message.getFrom()[0]).getAddress();
		}

		List<Address> toList = Lists.newArrayList();
		if(ArrayUtils.isNotEmpty(message.getAllRecipients())){
			toList = Arrays.asList(message.getAllRecipients());
		}
		Date sentDate = message.getSentDate();
		String content = null;
		String htmlContent = null;

		if(Boolean.valueOf(Objects.toString(queryForm.getAcceptContent()))){
			if(message.isMimeType("multipart/*")){
				MimeMultipart mimeMultipart = (MimeMultipart)message.getContent();

				for(int i =0; i < mimeMultipart.getCount(); i++){
					BodyPart bodyPart = mimeMultipart.getBodyPart(i);
					if(bodyPart.isMimeType("text/plain") && !Boolean.valueOf(Objects.toString(queryForm.getAcceptTextContent()))){
						continue;
					}
					if(bodyPart.isMimeType("text/html") && !Boolean.valueOf(Objects.toString(queryForm.getAcceptHtmlContent()))){
						continue;
					}

					Object bodyPartObj = bodyPart.getContent();
					if(bodyPart != null && bodyPartObj != null){
						if(bodyPart.isMimeType("text/plain")){
							content = bodyPartObj.toString();
						}

						if(bodyPart.isMimeType("text/html")){
							htmlContent = bodyPartObj.toString();
						}
					}
					bodyPartObj = null;
				}
			}else{
				content = String.valueOf(message.getContent());
			}
		}
		return new EmailMessage(StringUtils.trimToEmpty(subject), from, toList)
				.setSentDate(sentDate)
				.setOriginalMessage(message)
				.setContent(content)
				.setHtmlContent(htmlContent);
	}

	@SneakyThrows
	private EmailMessage parseMimeMessage(MimeMessage message, MailQueryForm queryForm){
		//log.info("parse mime message");
		MimeMessageParser parser = new MimeMessageParser(message).parse();
		// 获取邮件主题
		String subject = parser.getSubject();
		// 获取发件人地址
		String from = parser.getFrom();
		// 获取收件人地址
		List<Address> toList = Optional.ofNullable(parser.getTo()).orElse(Lists.newArrayList());

		Date sentDate = message.getSentDate();

		String content = null;
		String htmlContent = null;
		if(Boolean.valueOf(Objects.toString(queryForm.getAcceptContent()))){
			if(parser.hasHtmlContent() && Boolean.valueOf(Objects.toString(queryForm.getAcceptHtmlContent()))){
				htmlContent = parser.getHtmlContent();
			}
			if(parser.hasPlainContent() && Boolean.valueOf(Objects.toString(queryForm.getAcceptTextContent()))){
				content = parser.getPlainContent();
			}
		}
		return new EmailMessage(StringUtils.trimToEmpty(subject), from, toList)
				.setSentDate(sentDate)
				.setOriginalMessage(message)
				.setContent(content)
				.setHtmlContent(htmlContent);
	}

	/**
	 * 标记已读 imap支持邮件读写，pop3只支持读。
	 * @param messages
	 * @throws MessagingException
	 */
	@SneakyThrows
	public void flagRead(Message messages){
		log.info("设置 <{}> 为已读.", messages.getSubject());
		messages.setFlag(Flags.Flag.SEEN, true);
		//messages.saveChanges();
	}

	/**
	 * 标记已读 imap支持邮件读写，pop3只支持读。
	 * @param messages
	 * @throws MessagingException
	 */
	public void flagRead(Message... messages){
		for (Message m : messages) {
			flagRead(m);
		}
	}

	/**
	 * 是否新的邮件
	 * @param message
	 * @return
	 * @throws MessagingException
	 */
	@SneakyThrows
	private boolean isNewMail(Message message){
		Flags flags = message.getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.RECENT) {
                return true;
            }
        }
        return false;
	}

	@SneakyThrows
	private boolean isSeen(Message message){
		/*Flags flags = message.getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == Flags.Flag.SEEN) {
				return true;
			}
		}
		return false;*/
		return message.isSet(Flags.Flag.SEEN);
	}

	/**
	 * 下载附件
	 * @param parser
	 * @throws IOException
	 */
	private void downAttas(MimeMessageParser parser) throws IOException{
		// 获取附件，并写入磁盘
		List<DataSource> attachments = parser.getAttachmentList();
		for (DataSource ds : attachments) {
			String fileName = mailReceiveProperties.getAccessorySavePath() + File.separator + ds.getName();
			try(BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(fileName));
					BufferedInputStream ins = new BufferedInputStream(ds.getInputStream())) {

				byte[] data = new byte[2048];
				int length;
				while ((length = ins.read(data)) != -1) {
					outStream.write(data, 0, length);
				}
				outStream.flush();
				log.info("下载附件:" + fileName);
			}
		}
	}
}
