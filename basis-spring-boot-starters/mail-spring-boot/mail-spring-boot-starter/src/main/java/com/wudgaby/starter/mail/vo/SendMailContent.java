package com.wudgaby.starter.mail.vo;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class SendMailContent {
	@NonNull
	private MailAccount mailAccount;
	@NonNull
	private String[] sendTo;
	private String[] ccTos;
	@NonNull
	private String subject;
	@NonNull
	private String content;
	/**
	 * 附件路径
	 */
	private List<String> attachmentPathList;
}
