package com.wudgaby.platform.flowable.helper.mapper;

import com.github.pagehelper.Page;
import com.wudgaby.platform.flowable.helper.vo.TaskQueryVo;
import com.wudgaby.platform.flowable.helper.vo.TaskVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author : bruce.liu
 * @title: : IFlowableTaskDao
 * @projectName : flowable
 * @description: flowabletask的查询
 * @date : 2019/11/2316:34
 */
@Mapper
@Repository
public interface FlowableTaskDao {
    /**
     * 查询待办任务
     * @param params 参数
     * @return
     */
    Page<TaskVo> getApplyingTasks(TaskQueryVo params) ;

    /**
     * 查询已办任务列表
     * @param params 参数
     * @return
     */
    Page<TaskVo> getApplyedTasks(TaskQueryVo params) ;

}
