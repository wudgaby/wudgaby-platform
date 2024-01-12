package com.wudgaby.sample.oauth2server.controller;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/9 0009 10:24
 * @desc :
 */
@RestController
public class ManagerController {
    @Resource
    private UserDetailsManager userDetailsManager;

    @GetMapping("/addUser")
    public String addUser(){
        UserDetails userDetails = User.builder().passwordEncoder(s -> "{bcrypt}" + new BCryptPasswordEncoder().encode(s))
                .username("guest")
                .password("123456")
                .roles("ADMIN")
                .build();

        userDetailsManager.createUser(userDetails);
        return "添加用户成功";
    }

}
