package com.wudgaby.platform.sso.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wudgaby.platform.sso.server.entity.User;

/**
 * @ClassName : UserService
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/1 18:24
 * @Desc :   TODO
 */
public interface UserService extends IService<User> {
    User login(String account, String password);
}
