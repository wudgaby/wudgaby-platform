package com.wudgaby.platform.auth.service;

import com.wudgaby.platform.auth.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
public interface IRoleService extends IService<Role> {
    /**
     * 根据路径pattern 来获取 对应的角色.
     *
     * @param pattern the pattern
     * @return the set
     */
    Set<String> queryRoleByPattern(String pattern);

    Set<String> queryRoleByPatternAndMethod(String pattern, String method);

    Set<String> queryAllAvailable();
}
