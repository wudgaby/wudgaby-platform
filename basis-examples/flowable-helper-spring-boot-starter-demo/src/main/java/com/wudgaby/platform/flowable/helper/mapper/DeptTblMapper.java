package com.wudgaby.platform.flowable.helper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudgaby.platform.flowable.helper.entity.DeptTbl;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author zouyong
 * @since 2020-02-18
 */
public interface DeptTblMapper extends BaseMapper<DeptTbl> {
    /**
     * 根据用id 查询部门信息
     * @param userId
     * @return
     */
    DeptTbl getDeptByUserId(String userId);
}
