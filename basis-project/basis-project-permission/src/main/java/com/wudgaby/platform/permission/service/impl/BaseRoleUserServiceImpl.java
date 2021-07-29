package com.wudgaby.platform.permission.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.permission.entity.BaseRoleUser;
import com.wudgaby.platform.permission.mapper.BaseRoleUserMapper;
import com.wudgaby.platform.permission.service.BaseRoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统角色-用户关联 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseRoleUserServiceImpl extends ServiceImpl<BaseRoleUserMapper, BaseRoleUser> implements BaseRoleUserService {
    @Override
    public void addRoleUsers(Long roleId, List<Long> userIds) {
        this.remove(Wrappers.<BaseRoleUser>lambdaQuery().eq(BaseRoleUser::getRoleId, roleId));

        List<BaseRoleUser> baseRoleUserList = Lists.newArrayList();
        userIds.stream().forEach(userId -> {
            baseRoleUserList.add(new BaseRoleUser().setRoleId(roleId).setUserId(userId));
        });

        if(CollectionUtils.isNotEmpty(baseRoleUserList)) {
            this.saveBatch(baseRoleUserList);
        }
    }

    @Override
    public void addUserRoles(Long userId, List<Long> roleIds) {
        this.remove(Wrappers.<BaseRoleUser>lambdaQuery().eq(BaseRoleUser::getUserId, userId));

        List<BaseRoleUser> baseRoleUserList = Lists.newArrayList();
        roleIds.stream().forEach(roleId -> {
            baseRoleUserList.add(new BaseRoleUser().setRoleId(roleId).setUserId(userId));
        });

        if(CollectionUtils.isNotEmpty(baseRoleUserList)) {
            this.saveBatch(baseRoleUserList);
        }
    }
}
