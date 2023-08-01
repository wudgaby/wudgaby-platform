package com.wudgaby.starter.data.audit.config;

import com.wudgaby.starter.data.audit.audior.DataAuditEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 15:46
 * @desc :
 */
@EnableAsync
@Configuration
public class AuditConfig {
    @Bean
    @ConditionalOnMissingBean
    public DataAuditEventListener dataAuditEventListener() {
        return new DataAuditEventListener();
    }
}
