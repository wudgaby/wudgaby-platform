package com.wudgaby.platform.auth.service.impl;

import com.wudgaby.platform.auth.model.entity.Role;
import com.wudgaby.platform.auth.mapper.RoleMapper;
import com.wudgaby.platform.auth.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Override
    public Set<String> queryRoleByPattern(String pattern) {
        return this.baseMapper.findByUrl(pattern);
    }

    @Override
    public Set<String> queryRoleByPatternAndMethod(String url, String method) {
        return this.baseMapper.findByUrlAndMethod(url, method);
    }

    @Override
    public Set<String> queryAllAvailable() {
        List<Role> roleList = this.list();
        return roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());
    }
}
