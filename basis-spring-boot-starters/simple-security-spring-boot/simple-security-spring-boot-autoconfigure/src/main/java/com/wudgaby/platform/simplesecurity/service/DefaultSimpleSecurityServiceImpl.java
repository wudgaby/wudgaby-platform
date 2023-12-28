package com.wudgaby.platform.simplesecurity.service;

import com.wudgaby.platform.security.core.UserInfo;
import com.wudgaby.platform.simplesecurity.SecurityConst;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2022/1/10 0010 15:20
 * @Desc :  默认实现
 */
public class DefaultSimpleSecurityServiceImpl extends AbstractSimpleSecurityService {
    private static final String DEFAULT = "guest";
    @Override
    public UserInfo getLoginUser(String account, String password) {
        if(DEFAULT.equals(account) && DEFAULT.equals(password)) {
            return new UserInfo().setId(0).setAccount(account).setAdmin(false);
        }
        return null;
    }

    @Override
    public String[] getAdminRoleCodes() {
        return SecurityConst.ADMIN_ROLE_CODE_LIST;
    }
}
