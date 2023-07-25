package com.wudgaby.platform.simplesecurity;

import com.wudgaby.platform.security.core.UserInfo;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 15:20
 * @Desc :  默认实现
 */
public class DefaultSimpleSecurityServiceImpl extends AbstractSimpleSecurityService {
    private static final String DEFAULT = "default";
    @Override
    public UserInfo getLoginUser(String account, String password) {
        if(DEFAULT.equals(account) && DEFAULT.equals(password)) {
            return new UserInfo().setId(0).setAccount(account).setAdmin(false);
        }
        return null;
    }

    @Override
    public String[] getAdminRoleCodes() {
        return SecurityConst.ADMIN_CODE_LIST;
    }
}
