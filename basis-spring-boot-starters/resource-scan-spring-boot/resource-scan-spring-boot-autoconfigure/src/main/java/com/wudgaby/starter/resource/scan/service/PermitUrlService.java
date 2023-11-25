package com.wudgaby.starter.resource.scan.service;

import com.wudgaby.starter.resource.scan.ext.RequestMatcher;

import java.util.Collection;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/8/8 23:47
 * @Desc :
 */
public interface PermitUrlService {
    /**
     * 获取放行的url
     * @return
     */
    Collection<RequestMatcher> getPermitUrls();
}
