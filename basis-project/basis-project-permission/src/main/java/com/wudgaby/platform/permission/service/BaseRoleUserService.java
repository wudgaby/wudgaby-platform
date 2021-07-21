package com.wudgaby.platform.permission.service;

import com.wudgaby.platform.permission.entity.BaseRoleUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色-用户关联 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
public interface BaseRoleUserService extends IService<BaseRoleUser> {

    void addRoleUsers(Long roleId, List<Long> userIds);

    void addUserRoles(Long userId, List<Long> roleIds);
}
