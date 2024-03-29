package com.wudgaby.platform.sso.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.sso.server.entity.User;
import com.wudgaby.platform.sso.server.mapper.UserMapper;
import com.wudgaby.platform.sso.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/1 18:34
 * @Desc :
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(String account, String password) {
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));

        /*if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return user;
        }*/

        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        throw new BusinessException("账号或密码错误.");
    }
}
