package com.wudgaby.platform.auth.extend.dynamic.customer;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @ClassName : SecurityConfigAttributeLoader
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/11 12:29
 * @Desc :   TODO
 */
public interface SecurityConfigAttributeLoader {
    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> loadConfigAttribute(HttpServletRequest request);
}
