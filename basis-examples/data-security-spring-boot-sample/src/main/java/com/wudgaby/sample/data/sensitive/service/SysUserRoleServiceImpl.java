package com.wudgaby.sample.data.sensitive.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.sample.data.sensitive.entity.SysUserRole;
import com.wudgaby.sample.data.sensitive.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色关系表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}
