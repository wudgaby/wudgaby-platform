package com.wudgaby.platform.simplesecurity;

import com.wudgaby.platform.security.core.UserInfo;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 15:20
 * @Desc :  默认实现
 */
public class DefaultSimpleSecurityServiceImpl extends AbstractSimpleSecurityService {
    @Override
    public UserInfo getLoginUser(String account, String password) {
        if("default".equals(account) && "default".equals(password)) {
            return new UserInfo().setId(0).setAccount(account).setAdmin(false);
        }
        return null;
    }

    @Override
    public String[] getAdminRoleCodes() {
        return SecurityConst.ADMIN_CODE_LIST;
    }
}
