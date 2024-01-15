package com.wudgaby.starter.resource.scan.config;

import com.wudgaby.starter.resource.scan.scanner.ApiResourceScanner;
import com.wudgaby.starter.resource.scan.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/5/2 10:33
 * @Desc :  自动注册资源配置
 */
@Configuration
@ConditionalOnProperty(value = "resource.scan.enabled", havingValue = "true", matchIfMissing = true)
@Import({MqConfiguration.class, RedisSubscriberConfiguration.class})
@EnableConfigurationProperties(ResourceScanProperties.class)
public class ResourceScanConfiguration {
    /*@Bean
    @ConditionalOnMissingBean(RequestMappingScanListener.class)
    public RequestMappingScanListener requestMappingScanListener(ApiRegisterService apiRegisterService,
                                                                 @Autowired(required = false) PermitUrlService permitUrlService) {
        return new RequestMappingScanListener(apiRegisterService, permitUrlService);
    }*/

    @Bean
    @ConditionalOnMissingBean(ApiResourceScanner.class)
    public ApiResourceScanner apiResourceScanner(ApiRegisterService apiRegisterService) {
        return new ApiResourceScanner(apiRegisterService);
    }
    /**
     * redis 发布订阅模式
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ApiRegisterService.class)
    public ApiRegisterService apiRegisterService(ResourceScanProperties resourceScanProperties) {
        switch (resourceScanProperties.getType()){
            case REDIS_PUB_SUB:
                return new RedisApiPubService();
            case REDIS:
                return new RedisApiRegisterService();
            case MQ:
                return new MqApiRegisterService();
            case EVENT:
            default:
                return new EventApiRegisterService();
        }
    }
}
