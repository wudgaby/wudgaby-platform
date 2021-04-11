package com.wudgaby.platform.flowable.helper.service;

import com.wudgaby.platform.flowable.helper.vo.AuditNoticeVo;

/**
 * @ClassName : AuditNoticeService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/26 9:19
 * @Desc :   审批通知
 */
public interface AuditNoticeService {
    void caseAuditNotice(AuditNoticeVo auditNoticeVo);
    void disposeAuditNotice(AuditNoticeVo auditNoticeVo);
}
