package com.wudgaby.platform.flowable.helper.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableActivityCancelledEvent;
import org.flowable.engine.delegate.event.FlowableActivityEvent;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;
import org.springframework.stereotype.Component;

/**
 * @ClassName : GlobalActivityEventListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/27 9:18
 * @Desc :   全局监听器
 */
@Slf4j(topic = "FLOW_LOG")
@Component
public class GlobalActivityEventListener extends AbstractFlowableEngineEventListener {

    @Override
    protected void activityStarted(FlowableActivityEvent event) {
        FlowableActivityEventImpl eventImpl = (FlowableActivityEventImpl)event;
        log.info("活动已开始.activityName: {}", event.getActivityName());
    }

    @Override
    protected void activityCompleted(FlowableActivityEvent event) {
        FlowableActivityEventImpl eventImpl = (FlowableActivityEventImpl)event;
        log.info("活动已完成.activityName: {}", event.getActivityName());
    }

    @Override
    protected void activityCancelled(FlowableActivityCancelledEvent event) {
        FlowableActivityEventImpl eventImpl = (FlowableActivityEventImpl)event;
        log.info("活动已取消.activityName: {}", event.getActivityName());
    }

    @Override
    protected void activityCompensate(FlowableActivityEvent event) {
        log.info("活动补偿.{}", event.getActivityName());
    }

}
