package com.wudgaby.platform.simplesecurity.service;

import com.google.common.collect.Sets;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.security.core.SecurityConst;
import com.wudgaby.platform.security.core.SecurityUtils;
import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.springext.MetaResource;
import com.wudgaby.platform.springext.RequestContextHolderSupport;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.AntPathMatcher;

import java.util.*;

;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2022/1/10 0010 19:31
 * @Desc :
 */
public abstract class AbstractSimpleSecurityService implements SimpleSecurityService {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 获取用户权限值. 可重写
     * @return
     */
    @Override
    public Collection<String> getPermissionList() {
        UserInfo loginUser = (UserInfo) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGGED_USER);
        return Optional.ofNullable(loginUser).map(UserInfo::getAuthorities).orElse(Collections.emptyList());
    }

    /**
     * 获取用户角色值. 可重写
     * @return
     */
    @Override
    public Collection<String> getRoleList() {
        UserInfo loginUser = (UserInfo) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGGED_USER);
        return Optional.ofNullable(loginUser).map(UserInfo::getRoleCodes).orElse(Collections.emptyList());
    }

    @Override
    public Set<MetaResource> getMetaResourceList() {
        UserInfo loginUser = (UserInfo) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGGED_USER);
        return Optional.ofNullable(loginUser).map(UserInfo::getMetaResources).orElse(Sets.newHashSet());
    }

    /**
     * 判断是否是管理员. 可重写
     * @return
     */
    @Override
    public boolean checkAdmin() {
        UserInfo loginUser = (UserInfo) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGGED_USER);
        return loginUser.getAdmin();
    }

    /**
     * 判断用户是否有任意权限
     * @param requiredPermList
     * @return
     */
    @Override
    public boolean hasAnyPermission(String... requiredPermList) {
        Collection<String> userPermList = getPermissionList();
        for (String permission : requiredPermList) {
            if(hasPermission(userPermList, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否有任意角色
     * @param requiredRoleList
     * @return
     */
    @Override
    public boolean hasAnyRole(String... requiredRoleList) {
        Collection<String> userRoleList = getRoleList();
        return !CollectionUtils.intersection(userRoleList, Arrays.asList(requiredRoleList)).isEmpty();
    }

    /**
     * 判断用户是否有所有权限
     * @param requiredPermList
     * @return
     */
    @Override
    public boolean hasAllPermission(String... requiredPermList) {
        Collection<String> userPermList = getPermissionList();
        for (String permission : requiredPermList) {
            if(!hasPermission(userPermList, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断用户是否有所有角色
     * @param requiredRoleList
     * @return
     */
    @Override
    public boolean hasAllRole(String... requiredRoleList) {
        Collection<String> userRoleList = getRoleList();
        return CollectionUtils.intersection(userRoleList, Arrays.asList(requiredRoleList)).size() == requiredRoleList.length;
    }

    /**
     * 登录逻辑
     * @param account
     * @param password
     */
    @Override
    public void login(String account, String password) {
        UserInfo loginUser = getLoginUser(account, password);
        AssertUtil.notNull(loginUser, "账号密码错误");

        loginUser.setAdmin(SecurityUtils.isSuperAdmin());
        RequestContextHolderSupport.getRequest().getSession().setAttribute(SecurityConst.SESSION_LOGGED_USER, loginUser);
    }

    /**
     * 新增支持通配符
     * @param permission
     * @return
     */
    private boolean hasPermission(Collection<String> userPermList, String permission) {
        for (String userPerm : userPermList) {
            if (antPathMatcher.match(userPerm, permission)) {
                return true;
            }
        }
        return false;
    }
}
