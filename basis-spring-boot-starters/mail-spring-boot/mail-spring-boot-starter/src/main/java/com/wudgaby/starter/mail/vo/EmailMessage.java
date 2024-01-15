package com.wudgaby.starter.mail.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.mail.Address;
import javax.mail.Message;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class EmailMessage {
	@NonNull
	private String subject;
	@NonNull
	private String from;
	@NonNull
	private List<Address> toList;

	private Date sentDate;
	private Message originalMessage;

	private String content;
	private String htmlContent;
	private Boolean newMail;
	private Boolean seen;

	private String messageId;
	private Integer messageNo;

	@Override
	public String toString() {
		String sentDateStr = "no sent date";
		if(this.sentDate != null){
			sentDateStr = DateUtil.format(this.sentDate, DatePattern.NORM_DATETIME_PATTERN);
		}
		return MessageFormat.format("编号Id/No:{0}/{1} <{2}> - {3} - [{4}] 新邮件: {5}, 已读: {6}",
				messageId, messageNo, this.from, this.subject, sentDateStr, newMail, seen);
	}
}
