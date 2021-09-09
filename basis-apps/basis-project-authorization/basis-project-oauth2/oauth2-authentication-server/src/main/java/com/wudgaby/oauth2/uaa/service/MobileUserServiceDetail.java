package com.wudgaby.oauth2.uaa.service;

import com.wudgaby.oauth2.uaa.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassName : MobileUserServiceDetail
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/23 17:37
 * @Desc :   
 */
@Service("mobileUserServiceDetail")
public class MobileUserServiceDetail implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        return userMapper.findByPhone(mobile).convert();
    }
}
