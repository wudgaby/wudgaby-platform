package com.wudgaby.platform.auth.service;

import com.wudgaby.platform.auth.model.vo.MetaResource;

import java.util.Set;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/10 10:22
 * @Desc :   获取可用的资源的资源pattern 和 method 用以构建 AntPathRequestMatcher
 */
public interface MetaResourceService {
    /**
     * 获取对应uri 的ant  pattern  和请求方法.
     *
     * @return the map
     */
    Set<MetaResource> queryPatternsAndMethods();
}
