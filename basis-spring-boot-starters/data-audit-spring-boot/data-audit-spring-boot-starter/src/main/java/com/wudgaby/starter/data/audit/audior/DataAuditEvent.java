package com.wudgaby.starter.data.audit.audior;

import org.springframework.context.ApplicationEvent;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 15:47
 * @desc :
 */
public class DataAuditEvent extends ApplicationEvent {
    public DataAuditEvent(DataAuditConsumer source) {
        super(source);
    }

    public void apply() {
        DataAuditConsumer biFunction = (DataAuditConsumer)getSource();
        if (null != biFunction){
            biFunction.accept((paramObject1, paramObject2) -> DataAuditor.compare(paramObject1, paramObject2));
        }
    }
}
