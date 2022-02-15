package com.wudgaby.platform.simplesecurity.ext;

import com.wudgaby.platform.security.core.MetaResource;
import com.wudgaby.platform.simplesecurity.spring.RequestMatcher;

import java.util.Set;

/**
 * @ClassName : RequestMatcherCreator
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/10 10:19
 * @Desc :   
 */
public interface RequestMatcherCreator {

    /**
     * 转换为 reqMatcher
     *
     * @param metaResources
     * @return  reqMatcher
     */
    Set<RequestMatcher> convertToRequestMatcher(Set<MetaResource> metaResources);


}