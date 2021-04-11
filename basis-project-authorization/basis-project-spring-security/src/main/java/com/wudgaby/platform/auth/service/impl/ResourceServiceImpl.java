package com.wudgaby.platform.auth.service.impl;

import com.wudgaby.platform.auth.model.entity.Resource;
import com.wudgaby.platform.auth.mapper.ResourceMapper;
import com.wudgaby.platform.auth.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author WudGaby
 * @since 2020-04-17
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

}
