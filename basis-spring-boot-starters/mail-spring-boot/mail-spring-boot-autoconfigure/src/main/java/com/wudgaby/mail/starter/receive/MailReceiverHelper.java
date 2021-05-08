package com.wudgaby.mail.starter.receive;

import cn.hutool.core.util.ReUtil;
import com.google.common.base.Joiner;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.wudgaby.mail.starter.config.DefaultMailReceiveProperties;
import com.wudgaby.mail.starter.exception.MailServiceException;
import com.wudgaby.mail.starter.support.SimpleAuthenticator;
import com.wudgaby.mail.starter.vo.MailQueryForm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName : MailReceiverHelper
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/4/13/013 14:10
 * @Desc :   TODO
 */
@Slf4j
@Service
@AllArgsConstructor
public class MailReceiverHelper {
	private static Cache<String, Store> store_cache = CacheBuilder.newBuilder()
			.maximumSize(50)
			.expireAfterAccess(2, TimeUnit.MINUTES)
			.build();

	private final DefaultMailReceiveProperties mailReceiveProperties;

	/**
	 * 收取收件箱里的邮件
	 * 
	 * @param props
	 *            为邮件连接所需的必要属性
	 * @param authenticator
	 *            用户验证器
	 * @return Message数组（邮件数组）
	 */
	public Collection<Message> fetchInbox(Properties props, Authenticator authenticator, MailQueryForm queryForm){
		return fetchInbox(props, authenticator, null, queryForm);
	}

	/**
	 * 两个协议都尝试
	 * @param props
	 * @param authenticator
	 * @return
	 */
	public Collection<Message> fetchInboxEachProtocol(Properties props, Authenticator authenticator, MailQueryForm queryForm){
		String defaultProtocol = props.getProperty("mail.store.protocol");
		try{
			return fetchInbox(props, authenticator, defaultProtocol, queryForm);
		}catch (Exception ex){
			log.error(ex.getMessage());
		}
		String otherProtocol = Objects.equals(defaultProtocol, "pop3") ? "imap" : "pop3";
		return fetchInbox(props, authenticator, otherProtocol, queryForm);
	}

	/**
	 * 收取收件箱里的邮件
	 * 
	 * @param props
	 *            收取收件箱里的邮件
	 * @param authenticator
	 *            用户验证器
	 * @param protocol
	 *            使用的收取邮件协议，有两个值"pop3"或者"imap"
	 * @return Message数组（邮件数组）
	 */
	private Collection<Message> fetchInbox(Properties props, Authenticator authenticator, String protocol, MailQueryForm queryForm){
		//getDefaultInstance() 会拿到同一个session
		Session session = Session.getInstance(props, authenticator);
		try {
			String username = ((SimpleAuthenticator)authenticator).getUsername();
			Store store = store_cache.getIfPresent(username);
			if(store == null || !store.isConnected()){
				store = StringUtils.isBlank(protocol) ? session.getStore() : session.getStore(protocol);
				store.connect();
				store_cache.put(username, store);
			}
			log.info("store: {}", store);

			//转小写
			List<String> folderList = mailReceiveProperties.getFolderList().stream()
					.map((folderName -> StringUtils.lowerCase(folderName))).collect(Collectors.toList());

			if(CollectionUtils.isEmpty(folderList)){
				folderList = Lists.newArrayList();
			}

			if(queryForm != null && CollectionUtils.isNotEmpty(queryForm.getFolders())) {
				folderList.addAll(queryForm.getFolders().stream()
						.map((folderName -> StringUtils.lowerCase(folderName))).collect(Collectors.toList()));
			}

			Collection<Message> messageList = Lists.newArrayList();
			//POP3只有一个INBOX目录
			Folder[] folders = store.getDefaultFolder().list("*");
			log.info("{} 邮箱目录: {}", username, Joiner.on(",").join(Arrays.stream(folders).map(f -> f.getFullName()).iterator()));

			for(Folder folder : folders){
				String lowerCaseFolder = StringUtils.lowerCase(folder.getFullName());
				log.info("邮箱目录: {}", folder.getFullName());
				/*if(folderList.contains(lowerCaseFolder)){
					CollectionUtils.addAll(messageList, getFolderMessage(store, folder));
				}*/

				if(CollectionUtils.isEmpty(folderList)){
					Message[] messages = getFolderMessage(store, folder);
					if(ArrayUtils.isNotEmpty(messages)){
						CollectionUtils.addAll(messageList, messages);
					}
				}else{
					for(String queryFolder : folderList){
						if(ReUtil.contains(queryFolder, lowerCaseFolder)){
							Message[] messages = getFolderMessage(store, folder);
							if(ArrayUtils.isNotEmpty(messages)){
								CollectionUtils.addAll(messageList, messages);
							}
							break;
						}
					}
				}
			}
			return messageList;
		} catch (MessagingException e) {
			throw new MailServiceException("获取邮件失败!" + e.getMessage(), e);
		}
	}

	private Message[] getFolderMessage(Store store, Folder folder){
		try{
			if(folder == null){
				//获取收件箱
				folder = store.getFolder("INBOX");
			}
			log.info("获取 {} 目录邮件.", folder.getFullName());
			// 以读写方式打开
			if(!folder.isOpen()){
				folder.open(Folder.READ_WRITE);
			}
			Message[] messages = folder.getMessages();
			//imap才支持已读未读的区分
			log.info("{} 目录邮件中共 {} 封邮件! {}封新邮件! {}封未读!", folder.getFullName(), messages.length, folder.getNewMessageCount(), folder.getUnreadMessageCount());
			return messages;
		}catch (MessagingException e){
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
