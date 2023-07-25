package com.wudgaby.sample.data.sensitive.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.sample.data.sensitive.vo.UserForm;
import com.wudgaby.sample.data.sensitive.vo.UserRoleVo;
import com.wudgaby.sample.data.sensitive.entity.SysUser;
import com.wudgaby.sample.data.sensitive.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public List<UserRoleVo> getUserRoleList(Long userId) {
        return getBaseMapper().listUserRoles(userId);
    }

    @Override
    public void addUserForm(UserForm userForm) {
        this.getBaseMapper().addUserForm(userForm);
    }
}
