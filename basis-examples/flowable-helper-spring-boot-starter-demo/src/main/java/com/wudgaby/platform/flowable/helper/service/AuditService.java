package com.wudgaby.platform.flowable.helper.service;

import org.flowable.engine.delegate.DelegateExecution;

/**
 * @ClassName : AuditService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/20 12:35
 * @Desc :   TODO
 */
public interface AuditService {
    /**
     * 是否通过
     * @param execution
     * @return
     */
    boolean isApprove(DelegateExecution execution);

    /**
     * 投票结果
     * @param execution
     * @return
     */
    boolean getVoteResult(DelegateExecution execution) ;
}
