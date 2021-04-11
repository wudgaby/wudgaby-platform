package com.wudgaby.platform.sso.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.sso.server.entity.Resource;
import com.wudgaby.platform.sso.server.mapper.ResourceMapper;
import com.wudgaby.platform.sso.server.service.ResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author jimi
 * @since 2020-11-19
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    @Override
    public List<Resource> getPermissionList(Long userId, String sysCode) {
        return this.baseMapper.listResourceByUserId(userId, sysCode);
    }
}
