package com.wudgaby.platform.flowable.helper.service.impl;

import com.wudgaby.platform.flowable.helper.consts.FlowConstant;
import com.wudgaby.platform.flowable.helper.enums.AuditType;
import com.wudgaby.platform.flowable.helper.service.AuditService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

/**
 * @ClassName : AuditServiceImpl
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/20 12:39
 * @Desc :   
 */
@Service("auditService")
public class AuditServiceImpl implements AuditService {
    @Override
    public boolean isApprove(DelegateExecution execution) {
        String outcome = execution.getVariable(FlowConstant.OUTCOME, String.class);
        return AuditType.pass.name().equals(outcome);
    }

    @Override
    public boolean getVoteResult(DelegateExecution execution) {
        Boolean voteResult = execution.getVariable(FlowConstant.VOTE_RESULT, Boolean.class);
        return voteResult;
    }
}
