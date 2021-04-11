package com.wudgaby.platform.sso.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.sso.server.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author jimi
 * @since 2020-11-19
 */
public interface RoleService extends IService<Role> {
    List<String> getRoleListByUserId(Long userId);
}
