package com.wudgaby.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.sample.domain.SysRole;
import com.wudgaby.sample.service.SysRoleService;
import com.wudgaby.sample.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author wudgaby
* @description 针对表【sys_role(角色)】的数据库操作Service实现
* @createDate 2024-01-03 14:45:29
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

}




