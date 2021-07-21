package com.wudgaby.platform.permission.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.permission.entity.BaseRole;
import com.wudgaby.platform.permission.entity.BaseRoleUser;
import com.wudgaby.platform.permission.mapper.BaseRoleMapper;
import com.wudgaby.platform.permission.mapper.BaseRoleUserMapper;
import com.wudgaby.platform.permission.service.BaseRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.permission.service.BaseRoleUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统角色-基础信息 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
@Service
@AllArgsConstructor
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, BaseRole> implements BaseRoleService {
    private final BaseRoleUserService baseRoleUserService;
    private final BaseRoleUserMapper baseRoleUserMapper;

    @Override
    public Long addRole(BaseRole baseRole) {
        boolean isExist = this.count(Wrappers.<BaseRole>lambdaQuery().eq(BaseRole::getRoleCode, baseRole.getRoleCode())) > 0;
        AssertUtil.isFalse(isExist, "该编码已存在");

        this.save(baseRole);
        return baseRole.getRoleId();
    }

    @Override
    public void updateRole(BaseRole baseRole) {
        BaseRole dbBaseRole = this.getById(baseRole.getRoleId());
        AssertUtil.notNull(dbBaseRole, "该角色不存在");
        AssertUtil.isFalse(dbBaseRole.getRoleCode().equals(baseRole.getRoleCode()), "该编码已存在");

        this.updateById(baseRole);
    }

    @Override
    public void delRole(Long roleId) {
        BaseRole dbBaseRole = this.getById(roleId);
        AssertUtil.notNull(dbBaseRole, "该角色不存在");
        AssertUtil.isTrue(dbBaseRole.getIsPersist() == 0, "保留数据,该角色不允许删除");

        boolean hasMember = baseRoleUserService.count(Wrappers.<BaseRoleUser>lambdaQuery().eq(BaseRoleUser::getRoleId, dbBaseRole.getRoleId())) > 0;
        AssertUtil.isFalse(hasMember, "该角色下还有成员.不允许删除");

        this.removeById(roleId);
    }

    @Override
    public List<BaseRole> getUserRoles(Long userId) {
        return baseRoleUserMapper.selectRoleUserList(userId);
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return baseRoleUserMapper.selectRoleUserIdList(userId);
    }
}
