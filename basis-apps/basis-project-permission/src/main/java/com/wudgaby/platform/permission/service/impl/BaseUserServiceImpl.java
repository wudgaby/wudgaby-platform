package com.wudgaby.platform.permission.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.permission.entity.BaseUser;
import com.wudgaby.platform.permission.mapper.BaseUserMapper;
import com.wudgaby.platform.permission.service.BaseRoleUserService;
import com.wudgaby.platform.permission.service.BaseUserService;
import com.wudgaby.platform.utils.encryption.MD5Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统用户-管理员信息 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-17
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, BaseUser> implements BaseUserService {
    private final BaseRoleUserService baseRoleUserService;

    @Override
    public Long addUser(BaseUser baseUser) {
        boolean isExist = this.count(Wrappers.<BaseUser>lambdaQuery().eq(BaseUser::getUserName, baseUser.getUserName())) > 0;
        AssertUtil.isFalse(isExist, "该用户名已存在");

        baseUser.setPassword(MD5Util.encryptMD5(baseUser.getPassword()));
        this.save(baseUser);
        return baseUser.getUserId();
    }

    @Override
    public void updateUser(BaseUser baseUser) {
        BaseUser dbBaseUser = this.getById(baseUser.getUserId());
        AssertUtil.notNull(dbBaseUser, "该用户不存在");

        baseUser.setUserName(dbBaseUser.getUserName());
        baseUser.setPassword(MD5Util.encryptMD5(baseUser.getPassword()));
        baseUser.setStatus(dbBaseUser.getStatus());

        this.updateById(baseUser);
    }

}
