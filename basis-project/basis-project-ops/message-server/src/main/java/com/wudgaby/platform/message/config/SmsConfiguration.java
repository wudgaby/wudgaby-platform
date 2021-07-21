package com.wudgaby.platform.message.config;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.httpclient.PoolingHTTPClient;
import com.wudgaby.platform.message.config.properties.SmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : SmsConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 9:53
 * @Desc :
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfiguration {

    /**
     * 单发
     *
     * @param smsProperties
     * @return
     */
    @Bean
    public SmsSingleSender smsSingleSender(SmsProperties smsProperties) {
        // 创建一个连接池httpclient
        PoolingHTTPClient httpclient = new PoolingHTTPClient(Runtime.getRuntime().availableProcessors() * 2);
        return new SmsSingleSender(smsProperties.getAppId(), smsProperties.getAppKey(), httpclient);
    }

    /**
     * 群发
     *
     * @param smsProperties
     * @return
     */
    @Bean
    public SmsMultiSender smsMultiSender(SmsProperties smsProperties) {
        // 创建一个连接池httpclient
        PoolingHTTPClient httpclient = new PoolingHTTPClient(Runtime.getRuntime().availableProcessors() * 2);
        return new SmsMultiSender(smsProperties.getAppId(), smsProperties.getAppKey(), httpclient);
    }
}
