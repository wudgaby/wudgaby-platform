package com.wudgaby.platform.simplesecurity;

import com.wudgaby.platform.core.util.AssertUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 19:31
 * @Desc :
 */
public abstract class AbstractSimpleSecrityService implements SimpleSecurityService {
    /**
     * 只需要实现 通过账号/密码, 从存储的地方获取到用户信息即可.
     * @param account
     * @param password
     * @return
     */
    @Override
    abstract public LoginUser getLoginUser(String account, String password);

    /**
     * 设置管理员的role标识
     * @return
     */
    @Override
    abstract public String[] getAdminRoleCodes();

    /**
     * 获取用户权限值. 可重写
     * @return
     */
    @Override
    public Collection<String> getPermissionList(){
        LoginUser loginUser = (LoginUser) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGINED_USER);
        return loginUser.getAuthorities();
    }

    /**
     * 获取用户角色值. 可重写
     * @return
     */
    @Override
    public Collection<String> getRoleList(){
        LoginUser loginUser = (LoginUser) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGINED_USER);
        return loginUser.getRoles();
    }

    /**
     * 判断是否是管理员. 可重写
     * @return
     */
    @Override
    public boolean checkAdmin(){
        LoginUser loginUser = (LoginUser) RequestContextHolderSupport.getRequest().getSession().getAttribute(SecurityConst.SESSION_LOGINED_USER);
        return loginUser.getAdmin();
    }

    /**
     * 判断用户是否有权限
     * @param requiredPermList
     * @return
     */
    @Override
    public boolean hasPermission(String[] requiredPermList){
        Collection<String> userPermList = getPermissionList();
        return CollectionUtils.intersection(userPermList, Arrays.asList(requiredPermList)).size() > 0;
    }

    /**
     * 判断用户是否有角色
     * @param requiredRoleList
     * @return
     */
    @Override
    public boolean hasRole(String[] requiredRoleList){
        Collection<String> userRoleList = getRoleList();
        return CollectionUtils.intersection(userRoleList, Arrays.asList(requiredRoleList)).size() > 0;
    }

    /**
     * 登录逻辑
     * @param account
     * @param password
     */
    @Override
    public void login(String account, String password){
        LoginUser loginUser = getLoginUser(account, password);
        AssertUtil.notNull(loginUser, "账号密码错误");

        loginUser.verifyAdmin(getAdminRoleCodes());
        RequestContextHolderSupport.getRequest().getSession().setAttribute(SecurityConst.SESSION_LOGINED_USER, loginUser);
    }
}
