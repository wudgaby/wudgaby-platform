package com.wudgaby.platform.springext;

import java.util.Set;

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/10 10:19
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