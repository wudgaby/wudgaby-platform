package com.wudgaby.platform.flowable.helper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wudgaby.platform.flowable.helper.entity.UserRole;
import com.wudgaby.platform.flowable.helper.entity.UserTbl;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zouyong
 * @since 2020-02-18
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    List<String> findByRoleName(String roleName);

    /**
     * 根据角色id,获取用户的账户名
     * @param roleId
     * @return
     */
    List<UserTbl> getUserAccountByRoleId(String roleId);
}
