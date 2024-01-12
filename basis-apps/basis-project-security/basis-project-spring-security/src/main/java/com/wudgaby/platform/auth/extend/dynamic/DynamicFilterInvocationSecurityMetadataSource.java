package com.wudgaby.platform.auth.extend.dynamic;

import com.wudgaby.platform.auth.service.IRoleService;
import com.wudgaby.platform.auth.service.MetaResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/10 10:21
 * @Desc :   参考 ExpressionBasedFilterInvocationSecurityMetadataSource
 */
@Slf4j
public class DynamicFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Resource private RequestMatcherCreator requestMatcherCreator;
    @Resource private MetaResourceService metaResourceService;
    @Resource private IRoleService roleService;
    //@Resource private List<RequestMatcher> ignoringPatterns;

    /**
     * 根据提供的受保护对象的信息，其实就是 URI，获取该 URI 配置的所有角色
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 获取当前的请求
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        /*String reqUrl = request.getMethod() + " " + request.getRequestURI();
        log.info(reqUrl);
        for(RequestMatcher requestMatcher : ignoringPatterns){
            if(requestMatcher.matches(request)){
                log.info("不需要鉴权的url");
                return null;
            }
        }*/
        return role(request);
    }

    /**
     * url + method 转换成role name
     * @param request
     * @return
     */
    private Collection<ConfigAttribute> role(HttpServletRequest request){
        String reqUrl = request.getMethod() + " " + request.getRequestURI();
        // 这里可以放一个抽象接口来获取  request 配置的 ant pattern
        //获取所有button类型资源 转换成 RequestMatcher
        Set<RequestMatcher> requestMatchers = requestMatcherCreator.convertToRequestMatcher(metaResourceService.queryPatternsAndMethods());
        // 拿到其中一个  没有就算非法访问
        RequestMatcher reqMatcher = requestMatchers.stream().filter(requestMatcher -> requestMatcher.matches(request)).findAny()
                //.orElse(null)
                .orElseThrow(() -> new AccessDeniedException(reqUrl + " 无权限"));
        if(reqMatcher == null){
            return null;
        }

        AntPathRequestMatcher antPathRequestMatcher = (AntPathRequestMatcher) reqMatcher;
        // 根据pattern 获取该pattern被授权给的角色
        //String pattern = antPathRequestMatcher.getPattern();
        //Set<String> roles = roleService.queryRoleByPattern(pattern);

        Set<String> roles = roleService.queryRoleByPatternAndMethod(antPathRequestMatcher.getPattern(), request.getMethod());
        return CollectionUtils.isEmpty(roles) ? null : SecurityConfig.createList(roles.toArray(new String[0]));
    }

    /**
     * 获取全部角色
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<String> roles = roleService.queryAllAvailable();
        return CollectionUtils.isEmpty(roles) ? null : SecurityConfig.createList(roles.toArray(new String[0]));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
