package com.wudgaby.platform.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.sys.entity.SysUserRes;
import com.wudgaby.platform.sys.mapper.SysUserResMapper;
import com.wudgaby.platform.sys.service.SysUserResService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-资源关系表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-01-03
 */
@Service
public class SysUserResServiceImpl extends ServiceImpl<SysUserResMapper, SysUserRes> implements SysUserResService {

}
