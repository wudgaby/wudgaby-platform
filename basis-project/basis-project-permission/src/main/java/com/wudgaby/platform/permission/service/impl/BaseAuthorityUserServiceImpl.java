package com.wudgaby.platform.permission.service.impl;

import com.wudgaby.platform.permission.entity.BaseAuthorityUser;
import com.wudgaby.platform.permission.mapper.BaseAuthorityUserMapper;
import com.wudgaby.platform.permission.service.BaseAuthorityUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统权限-用户关联 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2021-07-15
 */
@Service
public class BaseAuthorityUserServiceImpl extends ServiceImpl<BaseAuthorityUserMapper, BaseAuthorityUser> implements BaseAuthorityUserService {

}
