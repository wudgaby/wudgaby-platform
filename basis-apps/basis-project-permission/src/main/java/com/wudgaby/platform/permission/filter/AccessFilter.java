package com.wudgaby.platform.permission.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.permission.consts.AuthorityConst;
import com.wudgaby.platform.permission.dto.AuthorityResource;
import com.wudgaby.platform.permission.dto.OpenAuthority;
import com.wudgaby.platform.permission.service.BaseAuthorityService;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.platform.webcore.spring.util.AntPathRequestMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/7/18 23:17
 * @Desc :
 */
@Slf4j
public class AccessFilter implements Filter{
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    private Boolean dynamicAccess = true;
    private static final ExcludeRegistry EXCLUDE_REGISTRY = ExcludeRegistry.ofStaticResource();
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final Map<String, Collection<AuthorityResource>> AUTHORITY_MAP = Maps.newHashMap();
    private List<AuthorityResource> authorityResourceList;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        //获取所有资源
        authorityResourceList = baseAuthorityService.findAuthorityResource();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        try{
            if(check(req)){
                chain.doFilter(request, response);
                return;
            }
            outputError((HttpServletResponse)response, HttpStatus.FORBIDDEN.value(), JacksonUtil.serialize(ApiResult.failure("无权限")));
        }catch (AccessDeniedException ae) {
            outputError((HttpServletResponse)response, HttpStatus.FORBIDDEN.value(), JacksonUtil.serialize(ApiResult.failure(ae.getMessage())));
        }
    }

    /**
     * 校验请求地址是否可通行
     * @param request
     * @return
     */
    private boolean check(HttpServletRequest request){
        String reqPath = request.getRequestURI();
        if(!Boolean.TRUE.equals(dynamicAccess)){
            return true;
        }
        if(isPermit(reqPath)){
            return true;
        }
        return checkAuthorities(request);
    }

    /**
     * 是否放行
     * @param reqPath
     * @return
     */
    private boolean isPermit(String reqPath){
        //是否在排除名单中
        boolean permit = EXCLUDE_REGISTRY.getAllExcludePatterns().stream()
                                            .anyMatch(p -> ANT_PATH_MATCHER.match(p, reqPath));
        if(permit) {
            return true;
        }

        //校验是否该地址需要认证
        return authorityResourceList.stream()
                .filter(res -> StringUtils.isNotBlank(res.getPath()))
                .anyMatch(res -> {
                    boolean hasAuth = BooleanUtils.toBooleanObject(res.getIsAuth());
                    return ANT_PATH_MATCHER.match(res.getPath(), reqPath) && !hasAuth;
                });
    }

    /**
     * 校验用户是否有权限
     * @param request
     * @return
     */
    private boolean checkAuthorities(HttpServletRequest request){
        UserInfo userInfo = SecurityUtils.getOptionalUser().orElseGet(() -> new UserInfo().setId(1418126524618072065L).setUsername("test"));
        if (AuthorityConst.ADMIN.equals(userInfo.getAccount())) {
            // 默认超级管理员账号,直接放行
            return true;
        }

        //从内充中获取当前用户所拥有的权限
        //Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();

        //动态获取path的权限编码
        Collection<ConfigAttribute> configAttributeCollection = getAuthorityResources(request);
        if(CollectionUtils.isEmpty(configAttributeCollection)) {
            return false;
        }

        //匹配的权限
        int countFindAuthority = 0 ;
        //过期的权限
        //int countExpiresAuthority = 0;
        //当前用户所拥有的权限
        List<OpenAuthority> openAuthorityList = baseAuthorityService.findAuthorityByUser(Long.parseLong(userInfo.getId().toString()), false);
        for(OpenAuthority openAuthority : openAuthorityList){
            for(ConfigAttribute configAttribute : configAttributeCollection){
                if(configAttribute.getAttribute().equals(openAuthority.getAuthority())){
                    countFindAuthority++;

                    if(Boolean.TRUE.equals(openAuthority.getIsExpired())) {
                        throw new AccessDeniedException("授权已过期");
                    }
                }
            }
        }
        return countFindAuthority > 0;
    }

    /**
     * 动态获取path的权限编码
     * !!无法解决相同请求路径 不同method的接口
     * @param request
     * @return
     */
    /*private Collection<ConfigAttribute> getAuthorityResources(HttpServletRequest request) {
        if(AUTHORITY_MAP.isEmpty()) {
            //获取所有资源
            List<AuthorityResource> authorityResourceList = baseAuthorityService.findAuthorityResource();
            for (AuthorityResource item : authorityResourceList) {
                String path = item.getPath();
                if (path == null) {
                    continue;
                }
                String fullPath = getFullPath(item.getServiceId(), path);
                item.setPath(fullPath);

                Collection<ConfigAttribute> configAttributeCollection = AUTHORITY_MAP.computeIfAbsent(fullPath, k -> Sets.newHashSet());
                configAttributeCollection.add(new SecurityConfig(item.getAuthority()));
            }
            log.info("=============加载动态权限:{}==============", authorityResourceList.size());
        }

        //获取path关联的权限编码
        AtomicReference<Collection<ConfigAttribute>> atomicReference = new AtomicReference<>();
        AUTHORITY_MAP.keySet().stream()
                .filter(path -> !"/**".equals(path))
                .filter(path -> ANT_PATH_MATCHER.match(path, request.getRequestURI()))
                .findFirst().ifPresent(path -> atomicReference.set(AUTHORITY_MAP.get(path)));
        return atomicReference.get();
    }*/

    /**
     * 动态获取path的权限编码
     * @param request
     * @return
     */
    private Collection<ConfigAttribute> getAuthorityResources(HttpServletRequest request) {
        if(AUTHORITY_MAP.isEmpty()) {
            //获取所有资源
            List<AuthorityResource> authorityResourceList = baseAuthorityService.findAuthorityResource();
            for (AuthorityResource item : authorityResourceList) {
                String path = item.getPath();
                if (path == null) {
                    continue;
                }
                //拆分多个url
                Splitter.on(",").splitToList(path).stream().forEach(p -> {
                    String fullPath = getFullPath(item.getServiceId(), p);
                    //item.setPath(fullPath);
                    AuthorityResource newAuthRes = new AuthorityResource();
                    newAuthRes.setPath(fullPath);
                    newAuthRes.setMethod(item.getMethod());
                    newAuthRes.setAuthority(item.getAuthority());
                    newAuthRes.setAuthorityId(item.getAuthorityId());
                    newAuthRes.setIsAuth(item.getIsAuth());
                    newAuthRes.setIsOpen(item.getIsOpen());
                    newAuthRes.setServiceId(item.getServiceId());
                    newAuthRes.setPrefix(item.getPrefix());
                    newAuthRes.setStatus(item.getStatus());
                    /*BeanUtils.copyProperties(item, newAuthRes);
                    newAuthRes.setPath(fullPath);*/

                    Collection<AuthorityResource> authorityResourceCollection = AUTHORITY_MAP.computeIfAbsent(fullPath, k -> Sets.newHashSet());
                    authorityResourceCollection.add(newAuthRes);
                });
            }
            log.info("=============加载动态权限:{}==============", authorityResourceList.size());
        }

        //获取path关联的权限编码
        AtomicReference<Collection<ConfigAttribute>> atomicReference = new AtomicReference<>();
        AUTHORITY_MAP.keySet().stream()
                .filter(path -> !"/**".equals(path))
                .filter(path -> ANT_PATH_MATCHER.match(path, request.getRequestURI()))
                .findFirst().ifPresent(path -> {
                    Collection<ConfigAttribute> configAttributeCollection = AUTHORITY_MAP.get(path)
                            .stream()
                            .filter(item -> {
                                //拆分多个method. GET,POST
                                return Splitter.on(",").splitToList(item.getMethod()).stream().anyMatch(m -> {
                                    String method = StringUtils.isNotBlank(m) ? m : null;
                                    return new AntPathRequestMatcher(path, method).matches(request);
                                });
                            })
                            .map(res -> new SecurityConfig(res.getAuthority()))
                            .collect(Collectors.toSet());

                    atomicReference.set(configAttributeCollection);
                }
        );

        return Optional.ofNullable(atomicReference.get()).orElseGet(() -> Sets.newHashSet());
    }

    private String getFullPath(String serviceId, String path) {
        return path;
    }

    private void outputError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(message);
        response.getWriter().flush();
    }

}
