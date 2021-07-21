package com.wudgaby.platform.flowable.helper.service;

import org.flowable.engine.delegate.DelegateExecution;

import java.util.List;

/**
 * @ClassName : VoteService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/19 14:45
 * @Desc :
 */
public interface VoteService {
    /**
     * 投票人
     * @param execution
     * @return
     */
    List<String> getVoters(DelegateExecution execution);

    /**
     * 投票结果
     * @param execution
     * @return
     */
    boolean isComplete(DelegateExecution execution);
}
