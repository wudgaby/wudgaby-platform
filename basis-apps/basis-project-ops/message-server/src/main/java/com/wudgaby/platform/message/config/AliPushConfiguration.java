package com.wudgaby.platform.message.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.wudgaby.platform.message.config.properties.AliProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/11/18 10:53
 * @Desc :
 */
@ConditionalOnClass(DefaultAcsClient.class)
@Configuration
public class AliPushConfiguration {
    private static final String REGION_ID = "cn-hangzhou";

    @Bean
    public DefaultAcsClient defaultAcsClient(AliProperties aliProperties){
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, aliProperties.getAccessKeyId(), aliProperties.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }
}
