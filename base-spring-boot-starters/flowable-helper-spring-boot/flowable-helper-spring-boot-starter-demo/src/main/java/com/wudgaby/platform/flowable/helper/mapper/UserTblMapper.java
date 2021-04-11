package com.wudgaby.platform.flowable.helper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wudgaby.platform.flowable.helper.entity.DeptTbl;
import com.wudgaby.platform.flowable.helper.entity.RoleTbl;
import com.wudgaby.platform.flowable.helper.entity.UserTbl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zouyong
 * @since 2020-02-17
 */
public interface UserTblMapper extends BaseMapper<UserTbl> {

}
