package com.wudgaby.platform.simplesecurity;

import com.wudgaby.platform.security.core.MetaResource;
import com.wudgaby.platform.security.core.UserInfo;

import java.util.Collection;
import java.util.Set;

public interface SimpleSecurityService {
    /**
     * 只需要实现 通过账号/密码, 从存储的地方获取到用户信息即可.
     * @param account
     * @param password
     * @return
     */
    UserInfo getLoginUser(String account, String password);

    /**
     * 设置管理员的role标识
     * @return
     */
    String[] getAdminRoleCodes();

    Collection<String> getPermissionList();

    /**
     * 获取用户角色值. 可重写
     * @return
     */
    Collection<String> getRoleList();

    /**
     * 获取用户资源
     * @return
     */
    Set<MetaResource> getMetaResourceList();

    /**
     * 判断是否是管理员. 可重写
     * @return
     */
    boolean checkAdmin();

    /**
     * 判断用户是否有权限
     * @param requiredPermList
     * @return
     */
    boolean hasAnyPermission(String[] requiredPermList);

    /**
     * 判断用户是否有角色
     * @param requiredRoleList
     * @return
     */
    boolean hasAnyRole(String[] requiredRoleList);

    /**
     * 判断用户是否有权限
     * @param requiredPermList
     * @return
     */
    boolean hasAllPermission(String[] requiredPermList);

    /**
     * 判断用户是否有角色
     * @param requiredRoleList
     * @return
     */
    boolean hasAllRole(String[] requiredRoleList);

    /**
     * 登录逻辑
     * @param account
     * @param password
     */
    void login(String account, String password);
}
