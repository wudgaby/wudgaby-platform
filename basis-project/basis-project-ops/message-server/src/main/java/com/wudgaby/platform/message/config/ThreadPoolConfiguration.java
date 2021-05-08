package com.wudgaby.platform.message.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @ClassName : ThreadPoolConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/10 10:39
 * @Desc :   异步调用线程池配置
 */
@Configuration
public class ThreadPoolConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(5);
        threadPool.setMaxPoolSize(50);
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        threadPool.setAwaitTerminationSeconds(60 * 15);
        threadPool.setThreadNamePrefix("async-");
        threadPool.initialize();
        return threadPool;
    }
}
