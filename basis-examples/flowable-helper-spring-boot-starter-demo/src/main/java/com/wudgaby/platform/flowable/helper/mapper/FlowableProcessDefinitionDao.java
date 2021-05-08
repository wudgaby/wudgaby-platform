package com.wudgaby.platform.flowable.helper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wudgaby.platform.flowable.helper.form.ProcessDefinitionQueryForm;
import com.wudgaby.platform.flowable.helper.vo.ProcessDefinitionVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author : bruce.liu
 * @title: : IFlowableProcessInstentDao
 * @projectName : flowable
 * @description: 流程定义Dao
 * @date : 2019/11/2317:09
 */
public interface FlowableProcessDefinitionDao extends BaseMapper {

    /**
     * 通过条件查询流程定义列表
     * @param params 参数
     * @return
     */
    //Page<ProcessDefinitionVo> getPagerModel(ProcessDefinitionQueryVo params);

    IPage<ProcessDefinitionVo> findProcessDefList(IPage iPage, @Param("query") ProcessDefinitionQueryForm queryForm);


    /**
     * 通过流程定义id获取流程定义的信息
     * @param processDefinitionId 流程定义id
     * @return
     */
    ProcessDefinitionVo getById(String processDefinitionId) ;
}
