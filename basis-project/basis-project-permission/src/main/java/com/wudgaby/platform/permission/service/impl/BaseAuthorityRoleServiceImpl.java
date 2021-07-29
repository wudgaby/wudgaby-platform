package com.wudgaby.platform.permission.service.impl;

import com.wudgaby.platform.permission.entity.BaseAuthorityRole;
import com.wudgaby.platform.permission.mapper.BaseAuthorityRoleMapper;
import com.wudgaby.platform.permission.service.BaseAuthorityRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统权限-角色关联 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAuthorityRoleServiceImpl extends ServiceImpl<BaseAuthorityRoleMapper, BaseAuthorityRole> implements BaseAuthorityRoleService {

}
