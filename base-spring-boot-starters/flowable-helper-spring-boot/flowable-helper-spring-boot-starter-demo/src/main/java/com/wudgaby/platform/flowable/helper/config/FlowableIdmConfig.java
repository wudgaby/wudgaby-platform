package com.wudgaby.platform.flowable.helper.config;

import com.wudgaby.platform.flowable.helper.identity.CustomGroupEntityManager;
import com.wudgaby.platform.flowable.helper.identity.CustomUserEntityManager;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.spring.SpringIdmEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 庄金明
 * @date 2020年3月24日
 */
@Configuration
public class FlowableIdmConfig implements EngineConfigurationConfigurer<SpringIdmEngineConfiguration> {

    @Override
    public void configure(SpringIdmEngineConfiguration configuration) {
        configuration.setGroupEntityManager(customGroupEntityManager(configuration));
        configuration.setUserEntityManager(customUserEntityManager(configuration));
    }

    @Bean
    public CustomGroupEntityManager customGroupEntityManager(IdmEngineConfiguration configuration) {
        return new CustomGroupEntityManager(configuration, configuration.getGroupDataManager());
    }

    @Bean
    public CustomUserEntityManager customUserEntityManager(IdmEngineConfiguration configuration) {
        return new CustomUserEntityManager(configuration, configuration.getUserDataManager());
    }
}
