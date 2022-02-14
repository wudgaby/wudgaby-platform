package com.wudgaby.platform.simplesecurity;

import com.google.common.collect.Sets;
import com.wudgaby.platform.core.util.AssertUtil;
import com.wudgaby.platform.simplesecurity.ext.MetaResource;
import com.wudgaby.platform.simplesecurity.ext.RequestContextHolderSupport;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 19:31
 * @Desc :
 */
public abstract class AbstractSimpleSecurityService implements SimpleSecurityService {

    /**
     * 获取用户权限值. 可重写
     * @return
     */
    @Override
    public Collection<String> getPermissionList() {
        LoginUser loginUser = (LoginUser) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGINED_USER);
        return Optional.ofNullable(loginUser).map(u -> u.getAuthorities()).orElse(Collections.emptyList());
    }

    /**
     * 获取用户角色值. 可重写
     * @return
     */
    @Override
    public Collection<String> getRoleList() {
        LoginUser loginUser = (LoginUser) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGINED_USER);
        return Optional.ofNullable(loginUser).map(u -> u.getRoles()).orElse(Collections.emptyList());
    }

    @Override
    public Set<MetaResource> getMetaResourceList() {
        LoginUser loginUser = (LoginUser) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGINED_USER);
        return Optional.ofNullable(loginUser).map(u -> u.getMetaResources()).orElse(Sets.newHashSet());
    }

    /**
     * 判断是否是管理员. 可重写
     * @return
     */
    @Override
    public boolean checkAdmin() {
        LoginUser loginUser = (LoginUser) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGINED_USER);
        return loginUser.getAdmin();
    }

    /**
     * 判断用户是否有任意权限
     * @param requiredPermList
     * @return
     */
    @Override
    public boolean hasAnyPermission(String[] requiredPermList) {
        Collection<String> userPermList = getPermissionList();
        return CollectionUtils.intersection(userPermList, Arrays.asList(requiredPermList)).size() > 0;
    }

    /**
     * 判断用户是否有任意角色
     * @param requiredRoleList
     * @return
     */
    @Override
    public boolean hasAnyRole(String[] requiredRoleList) {
        Collection<String> userRoleList = getRoleList();
        return CollectionUtils.intersection(userRoleList, Arrays.asList(requiredRoleList)).size() > 0;
    }

    /**
     * 判断用户是否有所有权限
     * @param requiredPermList
     * @return
     */
    @Override
    public boolean hasAllPermission(String[] requiredPermList) {
        Collection<String> userPermList = getPermissionList();
        return CollectionUtils.intersection(userPermList, Arrays.asList(requiredPermList)).size() == requiredPermList.length;
    }

    /**
     * 判断用户是否有所有角色
     * @param requiredRoleList
     * @return
     */
    @Override
    public boolean hasAllRole(String[] requiredRoleList) {
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
        LoginUser loginUser = getLoginUser(account, password);
        AssertUtil.notNull(loginUser, "账号密码错误");

        loginUser.verifyAdmin(getAdminRoleCodes());
        RequestContextHolderSupport.getRequest().getSession().setAttribute(SecurityConst.SESSION_LOGINED_USER, loginUser);
    }
}
