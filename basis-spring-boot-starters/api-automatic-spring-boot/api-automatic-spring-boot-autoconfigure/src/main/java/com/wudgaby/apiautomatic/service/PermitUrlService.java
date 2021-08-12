package com.wudgaby.apiautomatic.service;

import org.springframework.security.web.util.matcher.RequestMatcher;

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
