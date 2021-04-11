package com.wudgaby.oauth2.uaa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @ClassName : UserController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/23 14:40
 * @Desc :   TODO
 */
@Slf4j
@RestController
public class UserController {

    /**
     * 资源服务器提供的受保护接口
     * 获取当前账号信息
     * @param principal
     * @return
     */
    @GetMapping(value = "/user")
    public Principal getUser(Principal principal){
        log.info("{}", principal);
        return principal;
    }
}