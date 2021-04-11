package com.wudgaby.platform.auth.service.impl;

import com.wudgaby.platform.auth.model.entity.RoleResource;
import com.wudgaby.platform.auth.mapper.RoleResourceMapper;
import com.wudgaby.platform.auth.service.IRoleResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色-资源关系表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements IRoleResourceService {

}
