package com.wudgaby.flowable.module.web.config;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.job.service.SpringAsyncExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Slf4j
@Configuration
//@Deprecated
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration){
        engineConfiguration.setActivityFontName("宋体")
                           .setLabelFontName("宋体")
                           .setAnnotationFontName("宋体")
                           .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        //设置自定义的uuid生成策略
        engineConfiguration.setIdGenerator(uuidGenerator());
        engineConfiguration.setXmlEncoding("UTF-8");
        //启用任务关系计数
        engineConfiguration.setEnableTaskRelationshipCounts(true);
        //启动同步功能 一定要启动否则报错
        engineConfiguration.setAsyncExecutor(springAsyncExecutor());
        engineConfiguration.setDeploymentMode("single-resource");
    }

    @Bean
    public UuidGenerator uuidGenerator() {
        return new UuidGenerator();
    }

    @Bean
    public SpringAsyncExecutor springAsyncExecutor() {
        SpringAsyncExecutor springAsyncExecutor = new SpringAsyncExecutor();
        springAsyncExecutor.setTaskExecutor(processTaskExecutor());
        springAsyncExecutor.setDefaultAsyncJobAcquireWaitTimeInMillis(1000);
        springAsyncExecutor.setDefaultTimerJobAcquireWaitTimeInMillis(1000);
        return springAsyncExecutor;
    }

    @Bean
    public TaskExecutor processTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

}
