package com.wudgaby.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.sample.domain.SysTenant;
import com.wudgaby.sample.service.SysTenantService;
import com.wudgaby.sample.mapper.SysTenantMapper;
import org.springframework.stereotype.Service;

/**
* @author wudgaby
* @description 针对表【sys_tenant(租户)】的数据库操作Service实现
* @createDate 2024-01-03 14:45:29
*/
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant>
    implements SysTenantService{

}




