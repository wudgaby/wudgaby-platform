package com.wudgaby.platform.flowable.helper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wudgaby.platform.flowable.helper.vo.ProcessInstanceQueryVo;
import com.wudgaby.platform.flowable.helper.vo.ProcessInstanceVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author : bruce.liu
 * @title: : IFlowableProcessInstentDao
 * @projectName : flowable
 * @description: 
 * @date : 2019/11/2317:09
 */
public interface FlowableProcessInstanceDao extends BaseMapper {

    /**
     * 通过条件查询流程实例VO对象列表
     * @param params 参数
     * @return
     */
    //Page<ProcessInstanceVo> getPagerModel(ProcessInstanceQueryVo params);

    IPage<ProcessInstanceVo> listPage(IPage ipage, @Param("query") ProcessInstanceQueryVo query);

    ProcessInstanceVo getByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
}
