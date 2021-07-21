package com.wudgaby.platform.auth.service.impl;

import com.wudgaby.platform.auth.model.entity.User;
import com.wudgaby.platform.auth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassName : AccountUserService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 15:34
 * @Desc :
 */
@Service("accountUserService")
public class AccountUserService implements UserDetailsService {
    @Autowired private IUserService userService;
    private static int i = 0;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //因为角色的添加已经在用户实体类中加入了，所以查询到用户不为空，直接返回用户对象就可以了。
        User user = userService.getByAccount(username);
        //模拟不同平台,能登陆同一个账号
        /*if(i % 3 == 0){
            user.setPlatform("ANDROID");
        }else if(i % 3 == 1){
            user.setPlatform("IOS");
        }else{
            user.setPlatform("PC");
        }
        i++;*/

        if(user == null){
            throw new UsernameNotFoundException(username + " 不存在.");
        }
        return user.convert();
    }
}
