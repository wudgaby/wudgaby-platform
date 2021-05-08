package com.wudgaby.mail.starter.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class MailAccount {
	@NonNull
	private String userName;
	@NonNull
	private String password;

}
