package com.wudgaby.platform.sso.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.sso.server.entity.Role;
import com.wudgaby.platform.sso.server.mapper.RoleMapper;
import com.wudgaby.platform.sso.server.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author jimi
 * @since 2020-11-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public List<String> getRoleListByUserId(Long userId) {
        return this.baseMapper.listRoleListByUserId(userId);
    }
}
