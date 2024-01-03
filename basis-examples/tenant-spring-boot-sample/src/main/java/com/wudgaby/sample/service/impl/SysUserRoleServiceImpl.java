package com.wudgaby.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.sample.domain.SysUserRole;
import com.wudgaby.sample.service.SysUserRoleService;
import com.wudgaby.sample.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author wudgaby
* @description 针对表【sys_user_role(用户-角色关系表)】的数据库操作Service实现
* @createDate 2024-01-03 15:42:54
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
    implements SysUserRoleService{

}




