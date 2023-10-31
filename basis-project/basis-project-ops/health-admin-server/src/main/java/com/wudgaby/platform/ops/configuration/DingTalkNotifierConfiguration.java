package com.wudgaby.platform.ops.configuration;

import com.wudgaby.platform.ops.notifier.DingTalkNotifier;
import de.codecentric.boot.admin.server.config.AdminServerNotifierAutoConfiguration;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : DingTalkNotifierConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/8 9:40
 * @Desc :   TODO
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.boot.admin.notify.dingtask", name = "enabled", havingValue = "true")
@AutoConfigureBefore({AdminServerNotifierAutoConfiguration.NotifierTriggerConfiguration.class, AdminServerNotifierAutoConfiguration.CompositeNotifierConfiguration.class})
public class DingTalkNotifierConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("spring.boot.admin.notify.dingtask")
    @ConditionalOnProperty(prefix = "spring.boot.admin.notify.dingtask", name = "webhook-url")
    public DingTalkNotifier dingTaskNotifier(InstanceRepository repository) {
        return new DingTalkNotifier(repository);
    }

    /*@Primary
    @Bean(initMethod = "start", destroyMethod = "stop")
    public RemindingNotifier remindingNotifier(InstanceRepository repository) {
        RemindingNotifier notifier = new RemindingNotifier(dingTaskNotifier(repository), repository);
        notifier.setReminderPeriod(Duration.ofMinutes(10));
        notifier.setCheckReminderInverval(Duration.ofSeconds(10));
        return notifier;
    }*/
}
