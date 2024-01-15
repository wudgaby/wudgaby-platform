package com.wudgaby.starter.resource.scan.service;

import com.wudgaby.platform.springext.RequestMatcher;

import java.util.Collection;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/8/8 23:47
 * @Desc :
 */
public interface PermitUrlService {
    /**
     * 获取放行的url
     * @return
     */
    Collection<RequestMatcher> getPermitUrls();
}
