package com.wudgaby.platform.permission.service;

import com.wudgaby.platform.permission.entity.BaseRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色-基础信息 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
public interface BaseRoleService extends IService<BaseRole> {

    Long addRole(BaseRole baseRole);

    void updateRole(BaseRole baseRole);

    void delRole(Long roleId);

    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    List<BaseRole> getUserRoles(Long userId);

    /**
     * 获取用户角色ID列表
     *
     * @param userId
     * @return
     */
    List<Long> getUserRoleIds(Long userId);
}
