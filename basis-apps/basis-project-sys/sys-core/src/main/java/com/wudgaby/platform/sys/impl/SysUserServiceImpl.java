package com.wudgaby.platform.sys.impl;

import com.wudgaby.platform.sys.entity.SysUser;
import com.wudgaby.platform.sys.mapper.SysUserMapper;
import com.wudgaby.platform.sys.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
