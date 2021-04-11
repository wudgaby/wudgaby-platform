package com.wudgaby.platform.sys.impl;

import com.wudgaby.platform.sys.entity.SysTenant;
import com.wudgaby.platform.sys.mapper.SysTenantMapper;
import com.wudgaby.platform.sys.service.SysTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-09-28
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

}
