package com.wudgaby.starter.data.audit.audior;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 16:31
 * @desc :
 */
public class DataAuditEventListener {

    @Async
    @TransactionalEventListener(classes = DataAuditEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void listener(final DataAuditEvent dataAuditEvent) {
        dataAuditEvent.apply();
    }
}
