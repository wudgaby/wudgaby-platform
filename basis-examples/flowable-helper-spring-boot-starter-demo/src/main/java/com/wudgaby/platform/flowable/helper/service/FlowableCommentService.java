package com.wudgaby.platform.flowable.helper.service;

import com.wudgaby.platform.flowable.helper.vo.CommentVo;

import java.util.List;

/**
 * @author : bruce.liu
 * @projectName : flowable
 * @description: 审批意见service
 * @date : 2019/11/2412:55
 */
public interface FlowableCommentService {

    /**
     * 添加备注
     * @param comment 参数
     */
    void addComment(CommentVo comment) ;

    /**
     * 通过流程实例id获取审批意见列表
     * @param processInstanceId 流程实例id
     * @return
     */
    List<CommentVo> getFlowCommentVosByProcessInstanceId(String processInstanceId) ;

}
