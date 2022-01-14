package com.wudgaby.oauth2.uaa.service;

import com.wudgaby.oauth2.uaa.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassName : UserServiceDetail
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/23 17:37
 * @Desc :
 */
@Service("userServiceDetail")
public class UserServiceDetail implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.findByUserName(username);
    }
}
