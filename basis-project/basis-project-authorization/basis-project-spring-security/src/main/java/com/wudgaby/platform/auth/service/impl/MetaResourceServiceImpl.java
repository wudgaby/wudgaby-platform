package com.wudgaby.platform.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Sets;
import com.wudgaby.platform.auth.model.entity.Resource;
import com.wudgaby.platform.auth.model.vo.MetaResource;
import com.wudgaby.platform.auth.service.IResourceService;
import com.wudgaby.platform.auth.service.MetaResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @ClassName : MetaResourceServiceImpl
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/10 10:22
 * @Desc :   TODO
 */
@Service
public class MetaResourceServiceImpl implements MetaResourceService {
    @Autowired private IResourceService resourceService;

    @Override
    public Set<MetaResource> queryPatternsAndMethods() {
        Set<MetaResource> metaResources = Sets.newHashSet();

        List<Resource> buttonList = resourceService.list(new LambdaQueryWrapper<Resource>().ge(Resource::getResType, "BUTTON"));
        buttonList.stream().forEach(res -> {
            MetaResource e = new MetaResource();
            e.setPattern(res.getResUrl());
            e.setMethod(res.getResMethod());
            metaResources.add(e);
        });
        return metaResources;
    }
}
