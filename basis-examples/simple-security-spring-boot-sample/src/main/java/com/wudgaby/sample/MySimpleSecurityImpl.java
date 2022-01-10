package com.wudgaby.sample;

import com.google.common.collect.Lists;
import com.wudgaby.platform.simplesecurity.AbstractSimpleSecrityService;
import com.wudgaby.platform.simplesecurity.LoginUser;
import com.wudgaby.platform.simplesecurity.SimpleSecurityService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 17:42
 * @Desc :
 */

@Service
public class MySimpleSecurityImpl extends AbstractSimpleSecrityService{
    @Override
    public LoginUser getLoginUser(String account, String password) {
        return new LoginUser().setId(1).setAccount(account).setAdmin(false)
                .setRoles(Arrays.asList("role1"))
                .setAuthorities(Arrays.asList("sys:user:add", "sys:user:del"))
                ;
    }

    @Override
    public String[] getAdminRoleCodes() {
        return new String[]{"admin"};
    }
}