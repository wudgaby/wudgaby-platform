package com.wudgaby.platform.sys.impl;

import com.wudgaby.platform.sys.entity.SysUserPost;
import com.wudgaby.platform.sys.mapper.SysUserPostMapper;
import com.wudgaby.platform.sys.service.SysUserPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-职位关系表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements SysUserPostService {

}
