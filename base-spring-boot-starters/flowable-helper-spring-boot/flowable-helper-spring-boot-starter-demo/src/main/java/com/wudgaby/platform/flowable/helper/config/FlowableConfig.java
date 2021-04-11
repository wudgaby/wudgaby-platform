package com.wudgaby.platform.flowable.helper.config;

import com.wudgaby.platform.flowable.helper.extend.CustomProcessDiagramGenerator;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.identity.AuthenticationContext;
import org.flowable.common.engine.impl.async.DefaultAsyncTaskExecutor;
import org.flowable.common.engine.impl.identity.UserIdAuthenticationContext;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.job.service.SpringAsyncExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Slf4j
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {
    /*@Autowired
    private CustomDeploymentCache customDeploymentCache;*/

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration){
        /*engineConfiguration.setActivityFontName("宋体")
                           .setLabelFontName("宋体")
                           .setAnnotationFontName("宋体")
                           .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);*/

        //设置自定义的uuid生成策略
        engineConfiguration.setIdGenerator(uuidGenerator());
        engineConfiguration.setXmlEncoding("UTF-8");
        //启用任务关系计数
        engineConfiguration.setEnableTaskRelationshipCounts(true);
        //启动同步功能 一定要启动否则报错
        engineConfiguration.setAsyncExecutor(springAsyncExecutor());
        engineConfiguration.setProcessDiagramGenerator(new CustomProcessDiagramGenerator());
        //配置缓存
        //engineConfiguration.setProcessDefinitionCache(customDeploymentCache);

        /*try {
            engineConfiguration.setDeploymentMode("single-resource");
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources(ResourceLoader.CLASSPATH_URL_PREFIX + "/flowable/processes/*");
            engineConfiguration.setDeploymentResources(resources);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }*/
    }

    /**
     * 处理FlowableSecurityAutoConfiguration 使用SpringSecurityAuthenticationContext 导致的一些问题.
     * @return
     */
    @Bean
    public AuthenticationContext userIdAuthenticationContext(){
        return new UserIdAuthenticationContext();
    }

    @Bean
    public UuidGenerator uuidGenerator() {
        return new UuidGenerator();
    }

    @Bean
    public SpringAsyncExecutor springAsyncExecutor() {
        SpringAsyncExecutor springAsyncExecutor = new SpringAsyncExecutor();
        //springAsyncExecutor.setTaskExecutor(processTaskExecutor());
        springAsyncExecutor.setTaskExecutor(new DefaultAsyncTaskExecutor());
        springAsyncExecutor.setDefaultAsyncJobAcquireWaitTimeInMillis(1000);
        springAsyncExecutor.setDefaultTimerJobAcquireWaitTimeInMillis(1000);
        return springAsyncExecutor;
    }

    @Bean
    public TaskExecutor processTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
