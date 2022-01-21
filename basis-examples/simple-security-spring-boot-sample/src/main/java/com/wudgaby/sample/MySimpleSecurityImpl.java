package com.wudgaby.sample;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Sets;
import com.wudgaby.platform.simplesecurity.AbstractSimpleSecurityService;
import com.wudgaby.platform.simplesecurity.LoginUser;
import com.wudgaby.platform.simplesecurity.ext.MetaResource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 17:42
 * @Desc :
 */

@Service
public class MySimpleSecurityImpl extends AbstractSimpleSecurityService {
    @Override
    public LoginUser getLoginUser(String account, String password) {
        return new LoginUser().setId(1).setAccount(account).setAdmin(false)
                .setRoles(Arrays.asList("role1"))
                .setAuthorities(Arrays.asList("sys:user:add", "sys:user:del"))
                .setMetaResources(CollUtil.set(false,
                        new MetaResource( "/req1", HttpMethod.GET.name()),
                        new MetaResource( "/req2", "POST")
                ))
                ;
    }

    @Override
    public String[] getAdminRoleCodes() {
        return new String[]{"admin"};
    }
}
