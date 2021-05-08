package com.wudgaby.mail.starter.util;

import cn.hutool.core.util.ReUtil;
import com.wudgaby.mail.starter.config.DefaultMailReceiveProperties;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Properties;

@UtilityClass
public class MailUtil {
	public static Properties getMailPropertiesByEmail(String email, List<DefaultMailReceiveProperties.MailDomainProperties> supportMailList){
		String emailDomain = getMailDomain(email);

		if(CollectionUtils.isEmpty(supportMailList)){
			return null;
		}

		for(DefaultMailReceiveProperties.MailDomainProperties mailProp : supportMailList){
			if(CollectionUtils.isNotEmpty(mailProp.getDomains())){
				for(String domain : mailProp.getDomains()){
					if(ReUtil.contains(domain, emailDomain)){
						return mailProp.getProperties();
					}
				}
			}
		}
		return null;
	}

	public static String getMailDomain(String email){
		int begin = email.lastIndexOf("@");
		return email.substring(begin + 1);
	}

	public static String getMailAccount(String email){
		int begin = email.lastIndexOf("@");
		return email.substring(0, begin);
	}
}
