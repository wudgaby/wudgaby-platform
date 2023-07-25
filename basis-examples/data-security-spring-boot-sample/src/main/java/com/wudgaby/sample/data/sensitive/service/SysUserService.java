package com.wudgaby.sample.data.sensitive.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.sample.data.sensitive.vo.UserForm;
import com.wudgaby.sample.data.sensitive.vo.UserRoleVo;
import com.wudgaby.sample.data.sensitive.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
public interface SysUserService extends IService<SysUser> {
    List<UserRoleVo> getUserRoleList(Long userId);

    void addUserForm(UserForm userForm);
}
