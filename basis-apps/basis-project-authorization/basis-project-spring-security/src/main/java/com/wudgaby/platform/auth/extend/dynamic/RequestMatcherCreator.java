package com.wudgaby.platform.auth.extend.dynamic;

import com.wudgaby.platform.auth.model.vo.MetaResource;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
     * @param metaResources metaResource
     * @return  reqMatcher
     */
    Set<RequestMatcher> convertToRequestMatcher(Set<MetaResource> metaResources);


}