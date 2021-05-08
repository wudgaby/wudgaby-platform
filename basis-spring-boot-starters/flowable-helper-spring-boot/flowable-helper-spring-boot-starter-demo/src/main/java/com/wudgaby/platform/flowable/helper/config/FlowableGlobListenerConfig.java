package com.wudgaby.platform.flowable.helper.config;

import com.wudgaby.platform.flowable.helper.listener.GlobalActivityEventListener;
import com.wudgaby.platform.flowable.helper.listener.GlobalProcessEventListener;
import com.wudgaby.platform.flowable.helper.listener.GlobalTaskEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @ClassName : FlowableGlobListenerConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/27 9:27
 * @Desc :   全局监听配置 ContextRefreshedEvent在类被初始化之后触发
 */
@Configuration
public class FlowableGlobListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SpringProcessEngineConfiguration configuration;
    @Autowired
    private GlobalTaskEventListener globalTaskEventListener;
    @Autowired
    private GlobalProcessEventListener globalProcessEventListener;
    @Autowired
    private GlobalActivityEventListener globalActivityEventListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();
        //流程开始监听
        dispatcher.addEventListener(globalProcessEventListener, FlowableEngineEventType.PROCESS_STARTED);
        //流程创建监听
        dispatcher.addEventListener(globalProcessEventListener, FlowableEngineEventType.PROCESS_CREATED);
        //流程完成监听
        dispatcher.addEventListener(globalProcessEventListener, FlowableEngineEventType.PROCESS_COMPLETED);
        //流程取消监听
        dispatcher.addEventListener(globalProcessEventListener, FlowableEngineEventType.PROCESS_CANCELLED);

        //活动即将执行
        dispatcher.addEventListener(globalActivityEventListener, FlowableEngineEventType.ACTIVITY_COMPENSATE);
        //活动开始监听
        dispatcher.addEventListener(globalActivityEventListener, FlowableEngineEventType.ACTIVITY_STARTED);
        //活动完成监听
        dispatcher.addEventListener(globalActivityEventListener, FlowableEngineEventType.ACTIVITY_COMPLETED);
        //活动取消
        dispatcher.addEventListener(globalActivityEventListener, FlowableEngineEventType.ACTIVITY_CANCELLED);

        //任务创建监听
        dispatcher.addEventListener(globalTaskEventListener, FlowableEngineEventType.TASK_CREATED);
        //任务分配监听
        dispatcher.addEventListener(globalTaskEventListener, FlowableEngineEventType.TASK_ASSIGNED);
        //任务完成监听
        dispatcher.addEventListener(globalTaskEventListener, FlowableEngineEventType.TASK_COMPLETED);

        //多实例事件
        dispatcher.addEventListener(globalTaskEventListener, FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_CANCELLED);
        dispatcher.addEventListener(globalTaskEventListener, FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED);
        dispatcher.addEventListener(globalTaskEventListener, FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION);
        dispatcher.addEventListener(globalTaskEventListener, FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_STARTED);
    }
}