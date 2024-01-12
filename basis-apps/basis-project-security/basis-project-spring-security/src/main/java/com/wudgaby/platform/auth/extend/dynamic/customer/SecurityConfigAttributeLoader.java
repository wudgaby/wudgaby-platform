package com.wudgaby.platform.auth.extend.dynamic.customer;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/11 12:29
 * @Desc :
 */
public interface SecurityConfigAttributeLoader {
    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> loadConfigAttribute(HttpServletRequest request);
}
