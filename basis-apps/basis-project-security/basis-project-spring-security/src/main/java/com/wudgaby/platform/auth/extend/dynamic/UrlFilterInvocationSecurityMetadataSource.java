package com.wudgaby.platform.auth.extend.dynamic;

import com.wudgaby.platform.auth.model.entity.Resource;
import com.wudgaby.platform.auth.service.IResourceService;
import com.wudgaby.platform.auth.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/2/10 10:21
 * @Desc :   参考 ExpressionBasedFilterInvocationSecurityMetadataSource
 */
@Slf4j
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IResourceService resourceService;

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
        String reqUrl = request.getMethod() + " " + request.getRequestURI();
        log.info(reqUrl);

        return permission(request);
    }

    /**
     * url + method 获取权限code
     * 需要自定义AccessDecisionManager
     * @param request
     * @return
     */
    private Collection<ConfigAttribute> permission(HttpServletRequest request){
        List<Resource> permissions = resourceService.list();
        for(Resource res : permissions){
            if(new AntPathRequestMatcher(res.getResUrl(), res.getResMethod()).matches(request)){
                return SecurityConfig.createList(res.getPermCode());
            }
        }
        return null;
        //throw new AccessDeniedException("非法访问");
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
