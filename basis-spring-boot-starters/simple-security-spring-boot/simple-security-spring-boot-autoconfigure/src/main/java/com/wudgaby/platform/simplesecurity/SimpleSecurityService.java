package com.wudgaby.platform.simplesecurity;

import java.util.Collection;

public interface SimpleSecurityService {
    /**
     * 只需要实现 通过账号/密码, 从存储的地方获取到用户信息即可.
     * @param account
     * @param password
     * @return
     */
    LoginUser getLoginUser(String account, String password);

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
     * 判断是否是管理员. 可重写
     * @return
     */
    boolean checkAdmin();

    /**
     * 判断用户是否有权限
     * @param requiredPermList
     * @return
     */
    boolean hasPermission(String[] requiredPermList);

    /**
     * 判断用户是否有角色
     * @param requiredRoleList
     * @return
     */
    boolean hasRole(String[] requiredRoleList);

    /**
     * 登录逻辑
     * @param account
     * @param password
     */
    void login(String account, String password);
}
