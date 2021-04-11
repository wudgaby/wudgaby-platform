package com.wudgaby.platform.flowable.helper.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : bruce.liu
 * @projectName : flowable
 * @description: 运行时的节点Dao
 * @date : 2019/12/417:55
 */
@Mapper
@Repository
public interface RunFlowableActinstDao {

    /**
     * 删除节点信息
     * @param ids ids
     */
    public void deleteRunActinstsByIds(List<String> ids) ;
}
