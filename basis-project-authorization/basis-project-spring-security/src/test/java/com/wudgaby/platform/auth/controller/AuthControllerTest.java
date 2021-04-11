package com.wudgaby.platform.auth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName : AuthControllerTest
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/23 9:16
 * @Desc :   TODO
 */
class AuthControllerTest {

    @Test
    void usernameLogin() {
        System.out.println(new BCryptPasswordEncoder().encode("aa0000"));
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("aa0000"));
    }
}