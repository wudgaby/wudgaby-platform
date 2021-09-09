package com.wudgaby.platform.permission.mapper;

import com.wudgaby.platform.permission.entity.BaseRole;
import com.wudgaby.platform.permission.entity.BaseRoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统角色-用户关联 Mapper 接口
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
public interface BaseRoleUserMapper extends BaseMapper<BaseRoleUser> {
    /**
     * 查询系统用户角色
     *
     * @param userId
     * @return
     */
    List<BaseRole> selectRoleUserList(@Param("userId") Long userId);

    /**
     * 查询用户角色ID列表
     * @param userId
     * @return
     */
    List<Long> selectRoleUserIdList(@Param("userId") Long userId);
}
