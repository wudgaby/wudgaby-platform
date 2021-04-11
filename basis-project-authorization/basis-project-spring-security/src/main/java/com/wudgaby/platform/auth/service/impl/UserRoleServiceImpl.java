package com.wudgaby.platform.auth.service.impl;

import com.wudgaby.platform.auth.model.entity.UserRole;
import com.wudgaby.platform.auth.mapper.UserRoleMapper;
import com.wudgaby.platform.auth.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色关系表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
