package com.wudgaby.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.sample.domain.SysUser;
import com.wudgaby.sample.service.SysUserService;
import com.wudgaby.sample.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author wudgaby
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2024-01-03 14:45:29
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




