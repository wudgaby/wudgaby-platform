package com.wudgaby.starter.resource.scan.service;

import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;

import java.util.Collection;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/5/2 14:13
 * @Desc :
 */
public interface ApiRegisterService {
    /**
     * 批量注册
     * @param resourceDefinitionList
     */
    void batchRegister(Collection<ResourceDefinition> resourceDefinitionList);

}
