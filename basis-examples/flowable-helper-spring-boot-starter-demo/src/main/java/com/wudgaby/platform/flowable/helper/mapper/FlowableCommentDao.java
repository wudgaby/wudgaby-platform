package com.wudgaby.platform.flowable.helper.mapper;

import com.wudgaby.platform.flowable.helper.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : bruce.liu
 * @projectName : flowable
 * @description: 流程备注Dao
 * @date : 2019/11/2413:00
 */
@Mapper
@Repository
public interface FlowableCommentDao {

    /**
     * 通过流程实例id获取审批意见列表
     * @param ProcessInstanceId 流程实例id
     * @return
     */
    List<CommentVo> getFlowCommentVosByProcessInstanceId(String ProcessInstanceId);

}
